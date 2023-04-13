package com.heima.model.admin.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 管理员用户信息表
 * </p>
 *
 * @author itheima
 */
@Data
public class AdUserDto implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 登录用户名
     */
    private String name;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String image;

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
     * 最后一次登录时间
     */
    private Date loginTime;


}