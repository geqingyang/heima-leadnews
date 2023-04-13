package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.admin.entity.AdUser;
import com.heima.model.admin.dtos.AdUserLoginDto;

import java.util.Map;


public interface AdUserService extends IService<AdUser> {
    Map<String,Object> login(AdUserLoginDto userLoginDto);
}
