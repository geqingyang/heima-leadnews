package com.heima.model.article.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 文章信息表，存储已发布的文章
 * </p>
 *
 * @author itheima
 */

@Data
public class ArticleDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    /**
     * 标题
     */
    private String title;

    /**
     * 作者id
     */
    private Integer authorId;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 频道id
     */
    private Integer channelId;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 文章布局  0 无图文章   1 单图文章    2 多图文章
     */
    private Integer layout;

    /**
     * 文章标记  0 普通文章   1 热点文章   2 置顶文章   3 精品文章   4 大V 文章
     */
    private Integer flag;

    /**
     * 文章封面图片 多张逗号分隔
     */
    private String images;

    /**
     * 标签
     */
    private String labels;

    /**
     * 点赞数量
     */
    private Long likes;

    /**
     * 收藏数量
     */
    private Long collection;

    /**
     * 评论数量
     */
    private Long comment;

    /**
     * 阅读数量
     */
    private Long views;

    /**
     * 省市
     */
    private Integer provinceId;

    /**
     * 市区
     */
    private Integer cityId;

    /**
     * 区县
     */
    private Integer countyId;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 同步状态
     */
    private Boolean syncStatus;

    /**
     * 来源
     */
    private Boolean origin;
    /**
     * 是否可评论
     */
    private Boolean isComment;

    /**
     * 是否转发
     */
    private Boolean isForward;

    /**
     * 是否下架
     */
    private Boolean isDown;

    /**
     * 是否已删除
     */
    private Boolean isDelete;
    /**
     * 静态页面路径
     */
    private String staticUrl;

    /**
     * 自媒体新闻id
     */
    private Integer wmNewsId;

    /**
     * 文章内容
     */
    private String content;

    private Integer wmUserId;
}