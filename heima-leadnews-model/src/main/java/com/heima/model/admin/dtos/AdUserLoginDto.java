package com.heima.model.admin.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class AdUserLoginDto {

    /**
     * 用户名
     */

    @NotBlank(message = "用户名称不能为空")
    private String name;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 2,max = 16,message = "密码只能为2-16位")
    private String password;
}
