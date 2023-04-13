package com.heima.wemedia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 自媒体图文引用素材信息表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="WmNewsMaterial对象", description="自媒体图文引用素材信息表")
public class WmNewsMaterial implements Serializable {


      @ApiModelProperty(value = "主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty(value = "素材ID")
      private Integer materialId;

      @ApiModelProperty(value = "图文ID")
      private Integer newsId;

      @ApiModelProperty(value = "引用类型	            0 内容引用	            1 主图引用")
      private Integer type;

      @ApiModelProperty(value = "引用排序")
      private Integer ord;



}
