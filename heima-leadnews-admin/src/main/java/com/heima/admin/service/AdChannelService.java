package com.heima.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.admin.entity.AdChannel;
import com.heima.model.admin.dtos.ChannelDto;

public interface AdChannelService extends IService<AdChannel> {
    PageResult<ChannelDto> listByPage(ChannelDto dto);

    ResponseResult updateChannel(ChannelDto dto);


}
