package com.heima.wemedia.entity;

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
 * 自媒体图文数据统计表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="WmNewsStatistics对象", description="自媒体图文数据统计表")
public class WmNewsStatistics implements Serializable {


      @ApiModelProperty(value = "主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty(value = "主账号ID")
      private Integer userId;

      @ApiModelProperty(value = "子账号ID")
      private Integer article;

      @ApiModelProperty(value = "阅读量")
      private Integer readCount;

      @ApiModelProperty(value = "评论量")
      private Integer comment;

      @ApiModelProperty(value = "关注量")
      private Integer follow;

      @ApiModelProperty(value = "收藏量")
      private Integer collection;

      @ApiModelProperty(value = "转发量")
      private Integer forward;

      @ApiModelProperty(value = "点赞量")
      private Integer likes;

      @ApiModelProperty(value = "不喜欢")
      private Integer unlikes;

      @ApiModelProperty(value = "取消关注量")
      private Integer unfollow;

    private String burst;

      @ApiModelProperty(value = "创建时间")
      private Date createdTime;


}
