package com.heima.model.comment.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentLikeDto {

    /**
     * 评论id
     */
    private String commentId;

    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Integer operation;
}