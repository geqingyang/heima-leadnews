package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.article.entity.ApArticle;
import com.heima.model.article.dtos.ArticleDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 文章信息表，存储已发布的文章 Mapper 接口
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    @Select("select a.*,b.content as content from ap_article a " +
            "left join ap_article_content b on b.article_id=a.id " +
            "where a.is_delete=0 and a.is_down=0")
    IPage<ArticleDto> selectArticleContentByPage(IPage<ArticleDto> iPage);

    List<ArticleDto> findByPage(Page ipage);

    ArticleDto findById(Long articleId);
}
