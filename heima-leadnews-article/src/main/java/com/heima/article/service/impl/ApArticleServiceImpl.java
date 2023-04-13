package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.entity.ApArticle;
import com.heima.article.entity.ApArticleContent;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.util.BeanHelper;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.media.dtos.WmNewsDownOrUpDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务实现类
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    private final static short MAX_PAGE_SIZE = 10;

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ArticleFreemarkerService freemarkerService;



    @Autowired
    private ApArticleContentService contentService;



    @Override
    public Long createArticle(WmNewsResultDTO dto) {

        // 根据自媒体文章id查询article，有则update，没有则insert
        Integer wmNewsId =  dto.getId();
        ApArticle articleDB = getArticleByWmNewsId(wmNewsId);


        // article
        ApArticle articleToBeUpdate = buildApArticle(articleDB, dto);
        this.saveOrUpdate(articleToBeUpdate);

        // article content
        ApArticleContent articleContentToBeUpdate = buildArticleContent(articleToBeUpdate, dto);
        contentService.saveOrUpdate(articleContentToBeUpdate);
        //        发布成功后，业务解耦，发消息，异步，调用freemarkerApi 生成静态页面
        taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                freemarkerService.buildContentHtml(articleToBeUpdate, dto.getContent());
            }
        });


        return articleToBeUpdate.getId();
    }

    @Override
    public List<ArticleDto> load(ArticleHomeDto dto, int type) {
        //刷新条数
        Integer size = dto.getSize();
        if (size == null || size == 0){
            size = 10;
        }
        size = Math.min(size,MAX_PAGE_SIZE);

        Page<ApArticle> page = new Page<>(1,size);
        QueryWrapper<ApArticle> qw = new QueryWrapper<>();
        if (dto.getChannelId() != null && dto.getChannelId() != 0){
            qw.lambda().eq(ApArticle::getChannelId,dto.getChannelId());
        }

        if (type == 1){
            qw.lambda().lt(ApArticle::getPublishTime,dto.getMinTime());
        }else {
            qw.lambda().gt(ApArticle::getPublishTime,dto.getMaxTime());
        }
        qw.lambda().eq(ApArticle::getIsDown,false)
                .eq(ApArticle::getIsDelete,false)
                .orderByDesc(ApArticle::getPublishTime);
        //查询结果封装返回
        this.page(page,qw);
        if (page != null && !CollectionUtils.isEmpty(page.getRecords())){
            List<ArticleDto> articleDtos = BeanHelper.copyWithCollection(page.getRecords(), ArticleDto.class);
            return articleDtos;
        }
        return Collections.emptyList();
    }

    @Override
    public void upOrDownArticle(WmNewsDownOrUpDto dto) {
        UpdateWrapper<ApArticle> uw = new UpdateWrapper<>();
        uw.lambda().eq(ApArticle::getId,dto.getArticle())
                .set(ApArticle::getIsDown,!dto.getEnable());
        update(uw);
    }

    @Override
    public List<ArticleDto> findArticleByPage(int page, int size) {
        Page ipage = new Page(page, size);
        return this.getBaseMapper().findByPage(ipage);
    }

    @Override
    public ArticleDto findById(Long articleId) {
        return this.getBaseMapper().findById(articleId);
    }

    private ApArticleContent buildArticleContent(ApArticle article, WmNewsResultDTO dto) {
        ApArticleContent content = new ApArticleContent();
        content.setContent(dto.getContent());
        content.setArticleId(article.getId());
        return content;
    }

    private ApArticle buildApArticle(ApArticle articleDB, WmNewsResultDTO dto) {
        ApArticle apArticle = new ApArticle();
        apArticle.setId(articleDB==null?null:articleDB.getId());
        apArticle.setTitle(dto.getTitle());
        apArticle.setAuthorId(0);// todo 根据wm_user_id查询author
        apArticle.setAuthorName(null);
        apArticle.setChannelId(dto.getChannelId());
        apArticle.setChannelName(dto.getChannelName());
        apArticle.setLayout(dto.getType());
        apArticle.setFlag(0);// todo 枚举
        apArticle.setImages(dto.getImages());
        apArticle.setLabels(dto.getLabels());
        apArticle.setLikes(0L);
        apArticle.setCollection(0L);
        apArticle.setComments(0L);
        apArticle.setViews(0L);
        if(articleDB == null){
            apArticle.setCreatedTime(new Date());
        }
        apArticle.setPublishTime(new Date());
        apArticle.setIsComment(true);
        apArticle.setIsForward(true);
        apArticle.setIsDown(false);
        apArticle.setIsDelete(false);
        apArticle.setWmNewsId(dto.getId());
        return apArticle;
    }

    private ApArticle getArticleByWmNewsId(Integer wmNewsId) {
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApArticle::getWmNewsId, wmNewsId);
        return this.getOne(queryWrapper);
    }
}
