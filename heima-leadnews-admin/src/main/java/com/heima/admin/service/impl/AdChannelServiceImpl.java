package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.admin.entity.AdChannel;
import com.heima.model.admin.dtos.ChannelDto;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel>
        implements AdChannelService {

    @Override
    public PageResult<ChannelDto> listByPage(ChannelDto channelDto) {
//        构造分页条件
        Page<AdChannel> page = new Page(channelDto.getPage(),channelDto.getSize());
//        构造查询条件
        QueryWrapper<AdChannel> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(channelDto.getName())){
            queryWrapper.lambda().like(AdChannel::getName,channelDto.getName());
        }
//        分页查询
        IPage<AdChannel> adChannelIPage = this.page(page, queryWrapper);
//        如果没有返回值
        if(CollectionUtils.isEmpty(adChannelIPage.getRecords())){
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //            如果有返回值，对象转换
        List<ChannelDto> channelDtoList = BeanHelper.
                copyWithCollection(adChannelIPage.getRecords(), ChannelDto.class);
        return new PageResult<>(channelDto.getPage(),
                channelDto.getSize(),
                adChannelIPage.getTotal(),
                channelDtoList);

    }

    @Override
    public ResponseResult updateChannel(ChannelDto dto) {

        AdChannel adChannel = BeanHelper.copyProperties(dto, AdChannel.class);
        if (!this.updateById(adChannel)){
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR,"AdChannelService修改失败");
        }
        return ResponseResult.ok(AppHttpCodeEnum.SUCCESS);
    }

}
