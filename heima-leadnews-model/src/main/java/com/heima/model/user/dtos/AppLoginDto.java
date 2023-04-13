package com.heima.model.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppLoginDto {

    /**
     * 设备id
     */
    private String equipmentId;
    /**
     * 用户电话号码
     */
    private String phone;
    /**
     * 用户密码
     */
    private String password;
}