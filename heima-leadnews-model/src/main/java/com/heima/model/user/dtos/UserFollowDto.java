package com.heima.model.user.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * APP用户关注信息表
 * </p>
 *
 * @author itheima
 */
@Data
public class UserFollowDto implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 关注作者ID
     */
    private Integer followId;

    /**
     * 粉丝昵称
     */
    private String followName;

    /**
     * 关注度
     * 0 偶尔感兴趣
     * 1 一般
     * 2 经常
     * 3 高度
     */
    private Integer level;

    /**
     * 是否动态通知
     */
    private Boolean isNotice;


}