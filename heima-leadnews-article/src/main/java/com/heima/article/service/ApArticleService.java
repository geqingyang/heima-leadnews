package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.article.entity.ApArticle;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.media.dtos.WmNewsDownOrUpDto;
import com.heima.model.media.dtos.WmNewsResultDTO;

import java.util.List;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务类
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
public interface ApArticleService extends IService<ApArticle> {

    Long createArticle(WmNewsResultDTO dto);

    List<ArticleDto> load(ArticleHomeDto dto, int i);

    void upOrDownArticle(WmNewsDownOrUpDto wmNewsDownOrUpDto);

    List<ArticleDto> findArticleByPage(int page, int size);

    ArticleDto findById(Long articleId);
}
