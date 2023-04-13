package com.heima.model.article.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章行为消息内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleBehaviorMsg {
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 文章行为数量
     */
    private int  num;

    /**
     * 文章行为类型
     */
    private ArticleBehaviorType type;

    public enum ArticleBehaviorType {
        COLLECTION,COMMENT,LIKES,VIEWS;
    }
}
