package com.heima.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * APP用户信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-15
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="ApUser对象", description="APP用户信息表")
public class ApUser implements Serializable {


      @ApiModelProperty(value = "主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty(value = "用户名")
      private String name;

      @ApiModelProperty(value = "密码,Bcrypt加密")
      private String password;

      @ApiModelProperty(value = "手机号")
      private String phone;

      @ApiModelProperty(value = "头像")
      private String image;

      @ApiModelProperty(value = "0 男	            1 女	            2 未知")
      private Integer sex;

      @ApiModelProperty(value = "0 未	            1 是")
      private Boolean isCertification;

      @ApiModelProperty(value = "是否身份认证")
      private Boolean isIdentityAuthentication;

      @ApiModelProperty(value = "0 锁定           1正常")
      private Boolean status;

      @ApiModelProperty(value = "0 普通用户	            1 自媒体人	            2 大V")
      private Integer flag;

      @ApiModelProperty(value = "注册时间")
      private Date createdTime;


}
