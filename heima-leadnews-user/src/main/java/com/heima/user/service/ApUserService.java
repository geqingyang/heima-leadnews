package com.heima.user.service;

import com.heima.model.admin.dtos.UserLoginResultDto;
import com.heima.model.user.dtos.AppLoginDto;
import com.heima.model.user.dtos.AppLoginResultDto;
import com.heima.user.entity.ApUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.user.entity.ApUserLoginResultDto;

/**
 * <p>
 * APP用户信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-15
 */
public interface ApUserService extends IService<ApUser> {

    ApUserLoginResultDto login(AppLoginDto dto);
}
