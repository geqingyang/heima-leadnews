package com.heima.article.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP用户动态信息表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApDynamic对象", description="APP用户动态信息表")
public class ApDynamic implements Serializable {


      private Integer id;

      @ApiModelProperty(value = "文章作者的ID")
      private Integer userId;

      @ApiModelProperty(value = "作者昵称")
      private String userName;

      @ApiModelProperty(value = "频道名称")
      private String content;

      @ApiModelProperty(value = "是否转发")
      private Boolean isForward;

      @ApiModelProperty(value = "转发文章ID")
      private Long articleId;

      @ApiModelProperty(value = "转发文章标题")
      private String articelTitle;

      @ApiModelProperty(value = "转发文章图片")
      private String articleImage;

      @ApiModelProperty(value = "布局标识	            0 无图动态	            1 单图动态	            2 多图动态	            3 转发动态")
      private Integer layout;

      @ApiModelProperty(value = "文章图片	            多张逗号分隔")
      private String images;

      @ApiModelProperty(value = "点赞数量")
      private Integer likes;

      @ApiModelProperty(value = "收藏数量")
      private Integer collection;

      @ApiModelProperty(value = "评论数量")
      private Integer comment;

      @ApiModelProperty(value = "阅读数量")
      private Integer views;

      @ApiModelProperty(value = "创建时间")
      private Date createdTime;


}
