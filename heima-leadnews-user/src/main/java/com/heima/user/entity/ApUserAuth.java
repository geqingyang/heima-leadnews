package com.heima.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP实名认证信息表
 * </p>
 *
 * @author itheima
 */
@Data
public class ApUserAuth implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     0 创建中
     1 待审核
     2 审核失败
     9 审核通过
     */
    private Integer status;
    /**
     ** 手动添加
     */
    @Getter
    public enum Status{
        CREATEING(0),
        WAIT(1),
        FAIL(2),
        PASS(9),

        ;
        int code;

        Status(int code) {
            this.code = code;
        }
    }
    /**
     * 拒绝原因
     */
    private String reason;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 提交时间
     */
    private Date submitedTime;

    /**
     * 更新时间
     */
    private Date updatedTime;
}

