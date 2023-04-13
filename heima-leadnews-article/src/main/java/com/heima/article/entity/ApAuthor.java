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
 * APP文章作者信息表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApAuthor对象", description="APP文章作者信息表")
public class ApAuthor implements Serializable {


      @ApiModelProperty(value = "主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty(value = "作者名称")
      private String name;

      @ApiModelProperty(value = "0 爬取数据	            1 签约合作商	            2 平台自媒体人	            ")
      private Integer type;

      @ApiModelProperty(value = "社交账号ID")
      private Integer userId;

      @ApiModelProperty(value = "创建时间")
      private Date createdTime;

      @ApiModelProperty(value = "自媒体账号")
      private Integer wmUserId;


}
