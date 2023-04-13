package com.heima.model.article.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleBehaviorStreamMsg {

    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 阅读
     */
    private Integer views;
    /**
     * 收藏
     */
    private Integer collection;
    /**
     * 评论
     */
    private Integer comment;
    /**
     * 点赞
     */
    private Integer likes;
}
