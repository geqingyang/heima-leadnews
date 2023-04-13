package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.entity.AdUser;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JwtUtils;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.dtos.AdUserLoginDto;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class AdUserServiceImpl extends
        ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Map<String,Object> login(AdUserLoginDto userLoginDto) {
        //        构造查询条件
        QueryWrapper<AdUser> qw = new QueryWrapper<>();
        qw.lambda().eq(AdUser::getName,userLoginDto.getName());
        //        查询
        AdUser user = this.getOne(qw);
        //            如果根据用户名查询不到用户，报错
        if (user == null){
            throw new LeadException(AppHttpCodeEnum.AD_USER_NOT_EXIST,"用户名或密码错误");
        }
        //        使用BCryptPasswordEncoder提供的方法，比对密码
        boolean matches = encoder.matches(userLoginDto.getPassword(), user.getPassword());
        //            如果密码不匹配，报错
        if (!matches){
            throw new LeadException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"用户名或密码错误");
        }
        //        设置token和用户信息返回
        Map<String,Object> map  = new HashMap<>();
        String token = JwtUtils.generateTokenExpireInMinutes(user.getId(), 120);
        map.put("token",token);
        AdUserDto adUserDto = BeanHelper.copyProperties(user, AdUserDto.class);
        map.put("user",adUserDto);


        return map;
    }
}
