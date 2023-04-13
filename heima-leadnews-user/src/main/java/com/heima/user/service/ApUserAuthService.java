package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dtos.PageResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.user.entity.ApUserAuth;

public interface ApUserAuthService extends IService<ApUserAuth> {

    /**
     * 根据状态查询需要认证相关的用户信息
     * @param dto
     * @return
     */
    PageResult<UserAuthDto> loadListByStatus(AuthDto dto);

    /**
     * 修改审核状态
     * @param authDto
     * @param status 9-审核通过 2 审核失败
     */
    void updateAuthStatus(AuthDto authDto, int status);

}
