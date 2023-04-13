package com.heima.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * APP已发布文章内容表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApArticleContent对象", description="APP已发布文章内容表")
public class ApArticleContent implements Serializable {


      @ApiModelProperty(value = "文章ID")
      @TableId(value="article_id", type= IdType.INPUT)
        private Long articleId;

      @ApiModelProperty(value = "文章内容")
      private String content;


}
