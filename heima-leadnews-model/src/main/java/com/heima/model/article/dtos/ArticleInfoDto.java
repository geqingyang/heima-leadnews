package com.heima.model.article.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ArticleInfoDto {
    /**
     * 设备ID
     */
    String equipmentId;
    /**
     * 文章ID
     */
    @NotBlank
    Long articleId;
    /**
     * 作者ID
     */
    Integer authorId;
}