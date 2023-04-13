package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.admin.dtos.AdUserLoginDto;
import com.heima.model.admin.dtos.UserLoginResultDto;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.model.media.dtos.WmUserLoginDto;
import com.heima.model.media.dtos.WmUserLoginResultDto;
import com.heima.wemedia.entity.WmUser;

public interface WmUserService extends IService<WmUser> {
    Integer saveWmUser(WmUserDto dto);

    WmUserLoginResultDto login(WmUserLoginDto dto);
}
