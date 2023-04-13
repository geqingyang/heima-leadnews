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
 * 热文章表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApHotArticles对象", description="热文章表")
public class ApHotArticles implements Serializable {


      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private Integer entryId;

      @ApiModelProperty(value = "频道ID")
      private Integer tagId;

      @ApiModelProperty(value = "频道名称")
      private String tagName;

      @ApiModelProperty(value = "热度评分")
      private Integer score;

      @ApiModelProperty(value = "文章ID")
      private Long articleId;

      @ApiModelProperty(value = "省市")
      private Integer provinceId;

      @ApiModelProperty(value = "市区")
      private Integer cityId;

      @ApiModelProperty(value = "区县")
      private Integer countyId;

      @ApiModelProperty(value = "是否阅读")
      private Integer isRead;

    private Date releaseDate;

      @ApiModelProperty(value = "创建时间")
      private Date createdTime;


}
