package com.heima.model.article.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {
    /**
     * 最大时间
      */
    Date maxTime;
    /**
     * 最小时间
     */
    Date minTime;
    /**
     * 分页size
     */
    Integer size;
    /**
     * 数据范围，比如频道ID
     */
    Integer channelId;
}
