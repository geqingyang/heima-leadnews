package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JwtUtils;
import com.heima.model.admin.dtos.UserLoginResultDto;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.model.media.dtos.WmUserLoginResultDto;
import com.heima.model.user.dtos.AppLoginDto;
import com.heima.model.user.dtos.AppLoginResultDto;
import com.heima.user.entity.ApUser;
import com.heima.user.entity.ApUserLoginResultDto;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * APP用户信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-15
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public ApUserLoginResultDto login(AppLoginDto dto) {
        //根据用户名查询数据库
        LambdaQueryWrapper<ApUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApUser::getPhone,dto.getPhone());
        ApUser user = this.getOne(queryWrapper);
        if (user==null){
            throw new LeadException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        //密码匹配
        String passwordDB = user.getPassword();
        String passwordInput = dto.getPassword();
        boolean matches = encoder.matches(passwordInput,passwordDB);
        if (!matches){
            throw new LeadException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //生成token
        String token = JwtUtils.generateTokenExpireInMinutes(user.getId(),120);

        return new ApUserLoginResultDto(user,token);



    }
}
