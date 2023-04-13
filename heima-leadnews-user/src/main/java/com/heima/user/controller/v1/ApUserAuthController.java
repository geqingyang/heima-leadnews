package com.heima.user.controller.v1;

import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.user.service.ApUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ApUserAuthController {

    @Autowired
    private ApUserAuthService apUserAuthService;

    /**
     * 按照状态查询用户认证列表
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public PageResult<UserAuthDto> loadListByStatus(@RequestBody AuthDto dto){

        return apUserAuthService.loadListByStatus(dto);
    }

    /**
     * 审核通过
     * @param dto
     * @return
     */
    @PostMapping("/authPass")
    public ResponseResult authPass(@RequestBody AuthDto dto) {
        apUserAuthService.updateAuthStatus(dto, 9);
        return ResponseResult.ok();
    }
    /**
     * 审核不通过
     * @param dto
     * @return
     */
    @PostMapping("/authFail")
    public ResponseResult authFail(@RequestBody AuthDto dto) {
        apUserAuthService.updateAuthStatus(dto, 2);
        return ResponseResult.ok();
    }
}
