package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.message.WmNewsUpDownConstants;
import com.heima.common.dtos.PageResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.enums.WmNewsMaterialTypeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.threadlocal.UserThreadLocalUtils;
import com.heima.common.util.JsonUtils;
import com.heima.model.media.dtos.WmNewsContentItem;
import com.heima.model.media.dtos.WmNewsDownOrUpDto;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.dtos.WmNewsPageReqDto;
import com.heima.wemedia.entity.WmMaterial;
import com.heima.wemedia.entity.WmNews;
import com.heima.wemedia.entity.WmNewsMaterial;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.service.WmNewsAuditService;
import com.heima.wemedia.service.WmNewsMaterialService;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Autowired
    private WmNewsAuditService auditService;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;

    @Override
    public PageResult<WmNews> findByPage(WmNewsPageReqDto dto) {

        //构建查询条件
        QueryWrapper<WmNews> qw = new QueryWrapper<>();
        Integer userId = UserThreadLocalUtils.getUserId();
        if (userId!=null)
        { qw.lambda().eq(WmNews::getWmUserId,userId); }
        if(dto.getStatus() != null && dto.getStatus() != -1){
            if(dto.getStatus() ==1){
//             待审核包括 1-已提交待审核，3-待人工审核
                qw.lambda().in(WmNews::getStatus,new Integer[]{1,3});
            }
            else if(dto.getStatus() == 4){
//             审核通过 包括4-人工审核通过  ,待发布 8 审核通过（待发布） 9 已发布
                qw.lambda().in(WmNews::getStatus,new Integer[]{4,8,9});
            }else{
//                查询 草稿或 审核不通过
                qw.lambda().eq(WmNews::getStatus,dto.getStatus());
            }
        }
        if (dto.getChannelId() != null)
        { qw.lambda().eq(WmNews::getChannelId,dto.getChannelId()); }
        if (dto.getKeyword() != null)
        {qw.lambda().like(WmNews::getTitle,dto.getKeyword());}
        if (dto.getBeginPubDate()!=null)
        {qw.lambda().ge(WmNews::getPublishTime,dto.getBeginPubDate());}
        if (dto.getEndPubDate()!=null)
        { qw.lambda().le(WmNews::getPublishTime,dto.getEndPubDate()); }


        //分页查询
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        this.page(page,qw);

        //封装并返回结果
        PageResult<WmNews> pageResult = new PageResult<>(dto.getPage(),
                dto.getSize(),
                page.getTotal(),
                page.getRecords());
        return pageResult;
    }


    @Override
    public void submit(WmNewsDto wmNewsDto) {
        WmNews wmNews = buildNews(wmNewsDto);
        //判断文章id是否已存在
        if (wmNewsDto.getId() == null){
            //id不存在新增文章
            this.save(wmNews);
            wmNewsDto.setId(wmNews.getId());
            //保存文章素材关联关系
            List<WmNewsMaterial> newsMaterials = buildWmNewsMaterial(wmNewsDto);
            wmNewsMaterialService.saveBatch(newsMaterials);
        }else {
            //id存在,更新文章
            this.updateById(wmNews);
            //删除旧的素材关联关系
            wmNewsMaterialService.deleteByWmNewsId(wmNewsDto.getId());
            //插入新的关联关系
            List<WmNewsMaterial> newsMaterials = buildWmNewsMaterial(wmNewsDto);
            wmNewsMaterialService.saveBatch(newsMaterials);
        }
        log.info("文章新增或修改成功");
        //文章审核
        log.info("开始自媒体文章审核");
        auditService.auditWmNews(UserThreadLocalUtils.getUserId(), wmNewsDto);

    }

    @Override
    public void downOrUp(WmNewsDto dto) {
        //判断用户是否已登录
        Integer userId = UserThreadLocalUtils.getUserId();
        if (userId == null || userId ==0){
            throw new LeadException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //根据文章id和用户id构建条件
        QueryWrapper<WmNews> qw = new QueryWrapper<>();
        qw.lambda().eq(WmNews::getId,dto.getId())
                .eq(WmNews::getWmUserId,userId);
        WmNews news = getOne(qw);
        if (news == null){
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (news.getStatus() != 9){
            throw new LeadException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (dto.getEnable().equals(news.getEnable())){
            return;
        }
        //更新文章上下架状态
        UpdateWrapper<WmNews> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(WmNews::getId,dto.getId())
                .set(WmNews::getEnable,dto.getEnable());
        update(updateWrapper);

        WmNewsDownOrUpDto downOrUpDto = new WmNewsDownOrUpDto(news.getArticleId() ,dto.getEnable());
        log.info("发送消息到kafka,要求文章微服务更新文章上下架状态");
        kafkaTemplate.send(WmNewsUpDownConstants.WM_NEWS_UP_OR_DOWN_TOPIC, JsonUtils.toString(downOrUpDto));
    }

    private List<WmNewsMaterial> buildWmNewsMaterial(WmNewsDto dto) {
        List<WmNewsMaterial> result = new ArrayList<>();
        // 正文
        List<WmNewsMaterial> contentImages = buildWmNewsMaterialFromContent(dto.getId(), dto.getContent());
        if(!CollectionUtils.isEmpty(contentImages)){
            result.addAll(contentImages);
        }
        // 封面
        List<WmNewsMaterial> coverImages = buildWmNewsMaterialFromCover(dto.getId(), dto.getImages());
        if(!CollectionUtils.isEmpty(coverImages)){
            result.addAll(coverImages);
        }
        return result;

    }

    private List<WmNewsMaterial> buildWmNewsMaterialFromCover(Integer id, List<String> images) {
        if (CollectionUtils.isEmpty(images)){
            return null;
        }
        List<WmNewsMaterial> newsMaterials = new ArrayList<>();
        for (String url : images) {
            WmNewsMaterial material = buildWmNewsMaterial(id,url);
            newsMaterials.add(material);
        }
        return newsMaterials;

    }
    @Autowired
    private WmMaterialService wmMaterialService;

    private WmNewsMaterial buildWmNewsMaterial(Integer id, String url) {
        WmNewsMaterial material = new WmNewsMaterial();
        material.setType(WmNewsMaterialTypeEnum.SINGER_IMAGES.getType());
        material.setOrd(1);
        WmMaterial wmMaterial = wmMaterialService.findByUrl(url);
        if (wmMaterial != null ){
            material.setMaterialId(wmMaterial.getId());
        }else {
            log.error("根据url查询素材为空,url:"+url+",wmNewsId:"+id);
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        material.setNewsId(id);
        return material;
    }

    private List<WmNewsMaterial> buildWmNewsMaterialFromContent(Integer id, String content) {
        List<WmNewsContentItem> imageItems = WmNewsContentItem.getImageItems(content);
        if (CollectionUtils.isEmpty(imageItems)){
            return null;
        }

        return imageItems.stream().map(item ->{
            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setMaterialId(item.getId());
            wmNewsMaterial.setOrd(1);
            wmNewsMaterial.setNewsId(id);
            wmNewsMaterial.setType(WmNewsMaterialTypeEnum.NO_IMAGES.getType());
            return wmNewsMaterial;
        }).collect(Collectors.toList());

    }

    private WmNews buildNews(WmNewsDto wmNewsDto) {
        WmNews wmNews = new WmNews();
        if (wmNewsDto.getId() == null){
            wmNews.setCreatedTime(new Date());
        }
        if (!wmNewsDto.getDraft()){
            wmNews.setSubmitedTime(new Date());
        }
        wmNews.setId(wmNewsDto.getId());
        wmNews.setWmUserId(UserThreadLocalUtils.getUserId());
        wmNews.setTitle(wmNewsDto.getTitle());
        wmNews.setContent(wmNewsDto.getContent());
        wmNews.setType(wmNewsDto.getType());
        wmNews.setChannelId(wmNewsDto.getChannelId());
        wmNews.setLabels(wmNewsDto.getLabels());
        wmNews.setStatus(wmNewsDto.getStatus());
        wmNews.setArticleId(wmNewsDto.getArticleId());
        wmNews.setImages(CollectionUtils
                .isEmpty(wmNewsDto.getImages())?
                null:wmNewsDto.getImages().stream().collect(Collectors.joining(",")));
        wmNews.setEnable(wmNewsDto.getEnable());

        return wmNews;
    }


}
