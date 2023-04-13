package com.heima.admin.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 频道信息表
 * </p>
 *
 * @author HM
 * @since 2020-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdChannel extends Model<AdChannel> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 频道名称
     */
    private String name;

    /**
     * 频道描述
     */
    private String description;

    /**
     * 是否默认频道
     */
    private Boolean isDefault;
    /**
     * 是否启用 0- 不启用 1-启用
     */

    private Boolean status;

    /**
     * 默认排序
     */
    private Integer ord;

    /**
     * 创建时间
     */
    private Date createdTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
