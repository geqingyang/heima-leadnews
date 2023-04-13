package com.heima.user.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dtos.PageResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.enums.WeMediaStatusEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.feign.ArticleClient;
import com.heima.feign.WemediaFeign;
import com.heima.model.article.dtos.AuthorDto;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.user.entity.ApUser;
import com.heima.user.entity.ApUserAuth;
import com.heima.user.mapper.ApUserAuthMapper;
import com.heima.user.service.ApUserAuthService;
import com.heima.user.service.ApUserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ApUserAuthServiceImpl extends ServiceImpl<ApUserAuthMapper, ApUserAuth> implements ApUserAuthService {


    @Override
    public PageResult<UserAuthDto> loadListByStatus(AuthDto dto) {
//        设置查询条件
        QueryWrapper<ApUserAuth> queryWrapper = new QueryWrapper<>();
        if (dto != null && dto.getStatus() != null) {
            queryWrapper.lambda().eq(ApUserAuth::getStatus, dto.getStatus());
        }
//        设置分页条件
        IPage pageParam = new Page(dto.getPage(), dto.getSize());
//        分页查询
        IPage ipage = page(pageParam, queryWrapper);
        if(ipage == null || CollectionUtils.isEmpty(ipage.getRecords())){
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        List<UserAuthDto> userAuthDtoList = BeanHelper.copyWithCollection(ipage.getRecords(), UserAuthDto.class);
//        构造返回对象
        PageResult<UserAuthDto> pageResult = new PageResult(
                dto.getPage(),
                dto.getSize(),
                ipage.getTotal(),
                userAuthDtoList);
        return pageResult;
    }


    @Autowired
    private ArticleClient articleFeign;

    @Autowired
    private WemediaFeign wemediaFeign;

    @Autowired
    private ApUserService apUserService;

    @Override
    @GlobalTransactional
    public void updateAuthStatus(AuthDto authDto, int status) {
        //        验证参数，status当前方法中只接受9 和2
        if (status != 9
                && status != 2){
            throw new LeadException(AppHttpCodeEnum.PARAM_INVALID);
        }
        //        修改用户认证的状态
        ApUserAuth apUserAuth = new ApUserAuth();
        apUserAuth.setStatus(status);
        apUserAuth.setId(authDto.getId());
        apUserAuth.setReason(authDto.getMsg());
        boolean b = this.updateById(apUserAuth);
        if (!b){
            throw new LeadException(AppHttpCodeEnum.UPDATE_ERROR,"状态更新失败");
        }
        log.info("认证状态修改成功");
        //        如果认证不通过，直接返回
        if (status == WeMediaStatusEnum.FAILURE_AUDIT.getStatus()){
            return;
        }
        //        如果认证通过,获取用户id,获取用户信息
        ApUserAuth auth = getById(authDto.getId());
        ApUser apUser = apUserService.getById(auth.getUserId());
        //        远程调用wemedia，创建自媒体用户，传递WmUserDto对象
        WmUserDto wmUserDto = new WmUserDto();
        wmUserDto.setPhone(apUser.getPhone());
        wmUserDto.setName(apUser.getName());
        wmUserDto.setImage(apUser.getImage());
        wmUserDto.setPassword(apUser.getPassword());
        wmUserDto.setApUserId(apUser.getId());
        wmUserDto.setStatus(status);
        try {
            Integer wmUserId = wemediaFeign.saveWmUser(wmUserDto);
            wmUserDto.setId(wmUserId);
            log.info("创建自媒体用户成功");
        }catch (Exception e){
            log.error("创建自媒体用户失败");
            e.printStackTrace();
           throw new LeadException(AppHttpCodeEnum.SERVER_ERROR,e.getMessage());
        }
        //        远程调用article，创建文章用户，传递AuthDto对象
        AuthorDto authorDto = new AuthorDto();
        authorDto.setUserId(apUser.getId());
        authorDto.setName(apUser.getName());
        authorDto.setType(WeMediaStatusEnum.FAILURE_AUDIT.getStatus());
        authorDto.setWmUserId(wmUserDto.getId());
        try {
            articleFeign.createAuthor(authorDto);
            log.info("创建作者成功");
        }catch (Exception e){
            log.error("创建作者失败");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR,e.getMessage());
        }

        //        更新用户 类型
        ApUser apUser2 = new ApUser();
        apUser2.setId(apUser.getId());
        apUser2.setFlag(1);
        boolean b1 = apUserService.updateById(apUser2);
        if (!b1){
            throw new LeadException(AppHttpCodeEnum.UPDATE_ERROR,"用户类型更新失败");
        }


    }
}