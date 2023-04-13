package com.heima.article.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * APP设备码信息表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApEquipmentCode对象", description="APP设备码信息表")
public class ApEquipmentCode implements Serializable {


      private Integer id;

      @ApiModelProperty(value = "用户ID")
      private Integer equipmentId;

      @ApiModelProperty(value = "设备码")
      private String code;


}
