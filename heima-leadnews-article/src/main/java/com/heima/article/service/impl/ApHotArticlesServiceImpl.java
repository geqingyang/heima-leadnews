package com.heima.article.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.entity.ApArticle;
import com.heima.article.entity.ApHotArticles;
import com.heima.article.mapper.ApHotArticlesMapper;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ApHotArticlesService;
import com.heima.common.constants.article.ArticleConstans;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JsonUtils;
import com.heima.model.article.dtos.HotArticleVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 热文章表 服务实现类
 * </p>
 *
 * @author HM
 */
@Service
public class ApHotArticlesServiceImpl extends ServiceImpl<ApHotArticlesMapper, ApHotArticles> implements ApHotArticlesService {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 频道 新闻列表
     */
    private final String PRE_CHANNEL_FIX = "ld:article:hot:cid:";

    /**
     * 文章列表
     */
    private final String PRE_ARTICLE_FIX = "ld:article:aid";

    /**
     * 获取热点文章
     */
    @Override
    public void findHotArticle() {
//        计算5天前的时间
        String date = DateTime.now().minusDays(5).toString("yyyy-MM-dd 00:00:00");
//        条件前5天内的新发布文章
        QueryWrapper<ApArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .gt(ApArticle::getPublishTime,date)
                .eq(ApArticle::getIsDelete,false)
                .eq(ApArticle::getIsDown,false);
//        查询 文章
        List<ApArticle> apArticleList = apArticleService.list(queryWrapper);
        if(CollectionUtils.isEmpty(apArticleList)){
            return ;
        }
//        计算文章分值
        List<HotArticleVo> hotArticleVoList = dealHotApArticle(apArticleList);
//        把每个频道的文章信息缓存在redis中
        cacheHotArticleToRedisByChannel(hotArticleVoList);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 把热点文章 缓存在redis中
     * 分值和id 使用zset
     * 文章信息 使用hash
     * @param hotArticleVoList
     */
    private void cacheHotArticleToRedisByChannel(List<HotArticleVo> hotArticleVoList) {

        for (HotArticleVo hotArticleVo : hotArticleVoList) {
            String redisKey = PRE_CHANNEL_FIX + hotArticleVo.getChannelId();
            String json = JsonUtils.toString(hotArticleVo);
            redisTemplate.opsForZSet().add(redisKey,hotArticleVo.getId().toString(),hotArticleVo.getScore());
//        推荐频道 id是0，单独处理
            String redisKeyRecommend = PRE_CHANNEL_FIX + 0;
            redisTemplate.opsForZSet().add(redisKeyRecommend,hotArticleVo.getId().toString(),hotArticleVo.getScore());
//            把文章信息放入 redis缓存
            redisTemplate.opsForHash().put(PRE_ARTICLE_FIX,hotArticleVo.getId().toString(),json);
        }
    }

    /**
     * 计算文章的分值
     * @param apArticleList
     * @return
     */
    private List<HotArticleVo> dealHotApArticle(List<ApArticle> apArticleList) {
//        先把ApArticle 转HotArticleVo
        List<HotArticleVo> hotArticleVos = BeanHelper.copyWithCollection(apArticleList, HotArticleVo.class);
        for (HotArticleVo hotArticleVo : hotArticleVos) {
            computeScore(hotArticleVo);
        }
        return hotArticleVos;
    }

    /**
     * 计算文章分值
     * @param hotArticleVo
     */
    private void computeScore(HotArticleVo hotArticleVo) {
        long score = 0;
        if(hotArticleVo.getLikes()!= null){
//            点赞一次 3分
            score += hotArticleVo.getLikes() * ArticleConstans.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if(hotArticleVo.getViews() != null){
//          浏览一次  1分
            score += hotArticleVo.getViews();
        }
        if(hotArticleVo.getComment() != null){
//            评论一次 5分
            score += hotArticleVo.getComment() * ArticleConstans.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if(hotArticleVo.getCollection() != null){
//            收藏一次 8分
            score += hotArticleVo.getCollection() * ArticleConstans.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        hotArticleVo.setScore(Math.toIntExact(score));
    }
}

