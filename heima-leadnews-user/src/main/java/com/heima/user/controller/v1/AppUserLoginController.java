package com.heima.user.controller.v1;

import com.heima.common.dtos.ResponseResult;
import com.heima.model.admin.dtos.UserLoginResultDto;
import com.heima.model.user.dtos.AppLoginDto;
import com.heima.model.user.dtos.AppLoginResultDto;
import com.heima.user.entity.ApUserLoginResultDto;
import com.heima.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AppUserLoginController {

    @Autowired
    private ApUserService apUserService;

    @PostMapping("/api/v1/login/login_auth")
    public ResponseResult<ApUserLoginResultDto> login(@RequestBody AppLoginDto dto){
        return ResponseResult.ok(apUserService.login(dto));
    }
}
