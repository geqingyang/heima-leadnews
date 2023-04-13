package com.heima.wemedia.controller.v1;

import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.wemedia.entity.WmUser;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/wmuser")
public class WmUserController {

    @Autowired
    private WmUserService wmUserService;
    /**
     * 保存自媒体用户信息
     * @param dto
     * @return
     */
    @PostMapping("/save")
    public Integer saveWmUser(@Valid @RequestBody WmUserDto dto){
        return  wmUserService.saveWmUser(dto);
    }
}
