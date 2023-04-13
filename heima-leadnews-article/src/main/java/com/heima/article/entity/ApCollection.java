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
 * APP收藏信息表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApCollection对象", description="APP收藏信息表")
public class ApCollection implements Serializable {


      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      @ApiModelProperty(value = "用户id")
      private Integer apUserId;

      @ApiModelProperty(value = "设备号")
      private String equipmentId;

      @ApiModelProperty(value = "文章ID")
      private Long articleId;

      @ApiModelProperty(value = "收藏内容类型	            0文章	            1动态")
      private Integer type;

      @ApiModelProperty(value = "创建时间")
      private Date collectionTime;

      @ApiModelProperty(value = "发布时间")
      private Date publishedTime;


}
