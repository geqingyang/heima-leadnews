package com.heima.model.article.dtos;

import lombok.Data;

@Data
public class HotArticleVo extends ArticleDto {

    /**
     * 文章分值
     */
    private Integer score;
}
