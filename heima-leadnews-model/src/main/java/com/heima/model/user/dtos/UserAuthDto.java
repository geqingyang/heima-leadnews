package com.heima.model.user.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * APP实名认证信息表
 * </p>
 *
 * @author itheima
 */
@Data
public class UserAuthDto implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 账号ID
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 资源名称
     */
    private String idno;

    /**
     * 正面照片
     */
    private String fontImage;

    /**
     * 背面照片
     */
    private String backImage;

    /**
     * 手持照片
     */
    private String holdImage;

    /**
     * 活体照片
     */
    private String liveImage;

    /**
     * 状态
     * 0 创建中
     * 1 待审核
     * 2 审核失败
     * 9 审核通过
     */
    private Integer status;

    /**
     * 拒绝原因
     */
    private String reason;

}
