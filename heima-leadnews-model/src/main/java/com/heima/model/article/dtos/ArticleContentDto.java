package com.heima.model.article.dtos;

import lombok.Data;

@Data
public class ArticleContentDto {

    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;
}