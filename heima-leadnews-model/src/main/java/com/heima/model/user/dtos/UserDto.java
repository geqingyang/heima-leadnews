package com.heima.model.user.dtos;

import com.heima.common.util.constants.RegexPatterns;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * APP用户注册
 * </p>
 *
 * @author itheima
 */
@Data
public class UserDto {


    /**
     * 用户名
     */
    @Length(min = 6,max = 20,message = "用户名为6-20位")
    private String name;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 手机号
     */
    @Pattern(regexp = RegexPatterns.PHONE_REGEX,message = "手机号码格式不正确")
    private String phone;

    /**
     * 头像
     */
    private String image;

}