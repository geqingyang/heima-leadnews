package com.heima.model.media.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WmNewsDto {
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 频道id
     */
    private Integer channelId;
    /**
     * 标签
     */
    private String labels;
    /**
     * 发布时间
     */
    private Date publishTime;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章封面类型  0 无图 1 单图 3 多图 -1 自动
     */
    private Integer type;
    /**
     * 提交类型 提交为false --0  草稿为true --1
     */
    private Boolean draft;

    /**
     * 文章状态  0- 草稿  1-提交 待审核
     */
    private Integer status;
    /**
     * 封面图片列表 多张图以逗号隔开
     */
    private List<String> images;
    /**
     * 拒绝理由
     */
    private String reason;
    /**
     * 上下架操作
     */
    private Boolean enable;

    private Long articleId;

    //状态枚举类
    public enum Status {
        NORMAL(0), SUBMIT(1), FAIL(2), ADMIN_AUTH(3), ADMIN_SUCCESS(4), SUCCESS(8), PUBLISHED(9);
        Integer code;

        Status(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return this.code;
        }
    }
}