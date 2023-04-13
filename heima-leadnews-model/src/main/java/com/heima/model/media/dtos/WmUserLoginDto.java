package com.heima.model.media.dtos;

import com.heima.model.user.dtos.UserDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WmUserLoginDto {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String name;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;


}
