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
 * 文章标签信息表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApArticleLabel对象", description="文章标签信息表")
public class ApArticleLabel implements Serializable {


      @ApiModelProperty(value = "主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private Long articleId;

      @ApiModelProperty(value = "标签ID")
      private Integer labelId;

      @ApiModelProperty(value = "排序")
      private Integer count;


}
