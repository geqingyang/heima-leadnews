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
 * 搜索热词表
 * </p>
 *
 * @author HM
 * @since 2023-02-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApHotWords对象", description="搜索热词表")
public class ApHotWords implements Serializable {


      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty(value = "热词")
      private String hotWords;

      @ApiModelProperty(value = "0:热,1:荐,2:新,3:火,4:精,5:亮")
      private Integer type;

      @ApiModelProperty(value = "热词日期")
      private String hotDate;

      @ApiModelProperty(value = "创建时间")
      private Date createdTime;


}
