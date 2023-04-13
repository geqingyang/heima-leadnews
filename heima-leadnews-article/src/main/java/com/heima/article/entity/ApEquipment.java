package com.heima.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP设备信息表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApEquipment对象", description="APP设备信息表")
public class ApEquipment implements Serializable {


      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty(value = "0PC	            1ANDROID	            2IOS	            3PAD	            9 其他")
      private Boolean type;

      @ApiModelProperty(value = "设备版本")
      private String version;

      @ApiModelProperty(value = "设备系统")
      private String sys;

      @ApiModelProperty(value = "设备唯一号，MD5加密")
      private String no;

      @ApiModelProperty(value = "登录时间")
      private Date createdTime;


}
