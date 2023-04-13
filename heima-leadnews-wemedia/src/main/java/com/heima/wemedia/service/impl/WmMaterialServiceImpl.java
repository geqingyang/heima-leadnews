package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dtos.PageResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.threadlocal.UserThreadLocalUtils;
import com.heima.file.service.MinioService;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.wemedia.entity.WmMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial>
            implements WmMaterialService {

    @Autowired
    private MinioService minioService;


    @Override
    public Object uploadPicture(MultipartFile file) {
        Integer userId = UserThreadLocalUtils.getUserId();
        if (userId == null){
            throw new LeadException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //获取后缀,生成新的文件名
        String last =
                StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        String newName = UUID.randomUUID().toString() + last;
        //上传到minio
        try {
            String url = minioService.uploadImgFile("", newName, file.getInputStream());
            //保存到数据库
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUrl(url);
            wmMaterial.setUserId(userId);
            wmMaterial.setType(0);
            wmMaterial.setIsCollection(false);
            wmMaterial.setCreatedTime(new Date());
            boolean save = this.save(wmMaterial);
            if (!save){
                throw new LeadException(AppHttpCodeEnum.SERVER_ERROR,"uploadPicture,数据库保存素材失败");
            }
            return url;
        } catch (IOException e) {
            log.error("minio 上传失败！");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.UPLOAD_ERROR);
        }
    }

    @Override
    public PageResult<WmMaterial> findByPage(WmMaterialDto dto) {
        //获取用户id
        Integer userId = UserThreadLocalUtils.getUserId();
        if (userId == null){
            throw new LeadException(AppHttpCodeEnum.NEED_LOGIN);
        }
//        设置查询条件
        QueryWrapper<WmMaterial> qw = new QueryWrapper<>();
        //按照当前用户查询
        qw.lambda().eq(WmMaterial::getUserId,userId);
        //是否是收藏
        Integer isCollection = dto.getIsCollection();
        if (isCollection != null && isCollection == 1){
            qw.lambda().eq(WmMaterial::getIsCollection,isCollection);
        }
        //按照日期倒序
        qw.lambda().orderByDesc(WmMaterial::getCreatedTime);
        //设置分页条件
        Page<WmMaterial> page = new Page<>(dto.getPage(),dto.getSize());
//        分页查询
        this.page(page,qw);
        if (CollectionUtils.isEmpty(page.getRecords())){
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
//        获取分页结果集合
        return new PageResult<>(dto.getPage(),
                dto.getSize(),
                page.getTotal(),
                page.getRecords());
    }

    @Override
    public WmMaterial findByUrl(String url) {
        LambdaQueryWrapper<WmMaterial> qw = new LambdaQueryWrapper<>();
        qw.eq(WmMaterial::getUrl,url);
        return this.getOne(qw);

    }
}
