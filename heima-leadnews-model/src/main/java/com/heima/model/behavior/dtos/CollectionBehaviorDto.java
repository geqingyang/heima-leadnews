package com.heima.model.behavior.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CollectionBehaviorDto {
    /**
     * 设备ID
     */
    String equipmentId;
    /**
     * 文章、动态ID
     */
    Long articleId;
    /**
     * 收藏内容类型
     * 0文章
     * 1动态
     */
    Integer type;

    /**
     * 操作类型
     * 1收藏
     * 0取消收藏
     */
    Integer operation;

}