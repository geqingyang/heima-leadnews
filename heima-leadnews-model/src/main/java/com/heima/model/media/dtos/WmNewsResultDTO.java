package com.heima.model.media.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 自媒体图文内容信息
 * </p>
 *
 * @author itheima
 */
@Data
public class WmNewsResultDTO implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 自媒体用户ID
     */
    private Integer wmUserId;

    /**
     * 标题
     */
    private String title;

    /**
     * 图文内容
     */
    private String content;

    /**
     * 文章布局
     * 0 无图文章
     * 1 单图文章
     * 3 多图文章
     */
    private Integer type;

    /**
     * 图文频道ID
     */
    private Integer channelId;

    /**
     * 频道名称
     */
    private String channelName;

    private String labels;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 提交时间
     */
    private Date submitedTime;

    /**
     * 当前状态
     * 0 草稿
     * 1 提交（待审核）
     * 2 审核失败
     * 3 人工审核
     * 4 人工审核通过
     * 8 审核通过（待发布）
     * 9 已发布
     */
    private Integer status;

    /**
     * 定时发布时间，不定时则为空
     */
    private Date publishTime;

    /**
     * 拒绝理由
     */
    private String reason;

    /**
     * 发布库文章ID
     */
    private Long articleId;

    /**
     * //图片用逗号分隔
     */
    private String images;

    private Boolean enable;

    //状态枚举类
    public enum Status {
        NORMAL(0), SUBMIT(1), FAIL(2), ADMIN_AUTH(3), ADMIN_SUCCESS(4), SUCCESS(8), PUBLISHED(9);
        Integer code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    /**
     * 作者名称
     */
    private String authorName;

}