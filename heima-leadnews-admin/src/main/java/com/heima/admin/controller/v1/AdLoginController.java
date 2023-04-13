package com.heima.admin.controller.v1;


import com.heima.admin.service.AdUserService;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.admin.dtos.AdUserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class AdLoginController {
    @Autowired
    private AdUserService adUserService;

    /**
     * 管理平台用户登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login/in")
    public ResponseResult<Map<String,Object>> login(
            @Valid @RequestBody AdUserLoginDto userLoginDto){
        return ResponseResult.ok(adUserService.login(userLoginDto));
    }

}
