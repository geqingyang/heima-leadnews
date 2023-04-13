package com.heima.model.media.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 自媒体用户信息表
 * </p>
 *
 * @author itheima
 */
@Data
public class WmUserDto implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    private Integer apUserId;

    private Integer apAuthorId;

    /**
     * 登录用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String image;

    /**
     * 归属地
     */
    private String location;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态
     * 0 暂时不可用
     * 1 永久不可用
     * 9 正常可用
     */
    private Integer status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账号类型
     * 0 个人
     * 1 企业
     * 2 子账号
     */
    private Integer type;

    /**
     * 运营评分
     */
    private Integer score;

    /**
     * 最后一次登录时间
     */
    private Date loginTime;

}