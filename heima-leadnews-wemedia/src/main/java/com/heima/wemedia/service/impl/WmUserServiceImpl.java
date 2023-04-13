package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JwtUtils;
import com.heima.model.media.dtos.WmUserLoginResultDto;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.model.media.dtos.WmUserLoginDto;
import com.heima.wemedia.entity.WmUser;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser>
        implements WmUserService {

    /**
     * 保存/修改 自媒体用户信息
     * @param dto
     * @return
     */
    @Override
    public Integer saveWmUser(WmUserDto dto) {
//        根据ap_user_id 查询自媒体用户信息
        LambdaQueryWrapper<WmUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmUser::getApUserId,dto.getApUserId());
        WmUser wmUser = getOne(queryWrapper);
//        判断是否存在数据
        if(wmUser == null){
//            自媒体用户不存在，新增
            wmUser = new WmUser();
            wmUser.setApUserId(dto.getApUserId());
            wmUser.setCreatedTime(new Date());
            wmUser.setStatus(9);
        }
        wmUser.setName(dto.getName());
        wmUser.setPassword(dto.getPassword());
        wmUser.setPhone(dto.getPhone());
        //        新增或修改自媒体用户
        try{
            saveOrUpdate(wmUser);
        }catch (Exception e){
            e.printStackTrace();
            log.error("保存数据失败");
        }

        Integer wmUserId = wmUser.getId();
        return wmUserId;
    }

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public WmUserLoginResultDto login(WmUserLoginDto dto) {
        //根据用户名查询数据库
        LambdaQueryWrapper<WmUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmUser::getName,dto.getName());
        WmUser user = this.getOne(queryWrapper);
        if (user==null){
            throw new LeadException(AppHttpCodeEnum.AD_USER_NOT_EXIST);
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

        return new WmUserLoginResultDto(BeanHelper.copyProperties(user, WmUserDto.class),token);

    }
}
