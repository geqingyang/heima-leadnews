package com.heima.wemedia.controller.v1;


import com.heima.common.dtos.ResponseResult;
import com.heima.model.admin.dtos.AdUserLoginDto;
import com.heima.model.admin.dtos.UserLoginResultDto;
import com.heima.model.media.dtos.WmUserLoginDto;
import com.heima.model.media.dtos.WmUserLoginResultDto;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {
    @Autowired
    private WmUserService wmUserService;

    /**
     * 管理平台用户登录
     * @param
     * @return
     */
    @PostMapping("/login/in")
    public ResponseResult<WmUserLoginResultDto> login(
            @Valid @RequestBody WmUserLoginDto dto){
        WmUserLoginResultDto login = wmUserService.login(dto);
        return ResponseResult.ok(login);
    }
}
