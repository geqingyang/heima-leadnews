package com.heima.admin.controller.v1;


import com.heima.admin.service.AdChannelService;
import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.admin.entity.AdChannel;
import com.heima.model.admin.dtos.ChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Autowired
    private AdChannelService adchannelService;

    /**
     * 分页查询频道信息
     * */
    @PostMapping("/list")
    public PageResult<ChannelDto> listByPage(@RequestBody ChannelDto dto){

        return adchannelService.listByPage(dto);
    }

    /**
     * 新增频道
     * */
    @PostMapping("/save")
    public ResponseResult save(@Valid @RequestBody ChannelDto dto){
        //对象转换
        AdChannel adChannel = BeanHelper.copyProperties(dto, AdChannel.class);
        adChannel.setCreatedTime(new Date());
        //根据结果返回
        if (!adchannelService.save(adChannel)){
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR,"新增失败");
        }
        return ResponseResult.ok(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 修改频道信息
     * @param dto
     * @return
     */
    @PostMapping("update")
    public ResponseResult updateChannel(@Valid @RequestBody ChannelDto dto){
        return adchannelService.updateChannel(dto);
    }

    /**
     * 删除频道信息，如果频道状态是有效，不能删除
     * @return
     */
    @PostMapping("/del/{id}")
    public ResponseResult deleteChannel(@PathVariable Integer id){
        AdChannel adChannel = adchannelService.getById(id);
        if (adChannel == null){
            throw new LeadException(AppHttpCodeEnum.PARAM_INVALID,"该频道ID不存在");
        }
        if (adChannel.getStatus()){
            throw new LeadException(AppHttpCodeEnum.DATA_CAN_NOT_DEL,"当前频道状态为有效,不能删除!");
        }
        if (!adchannelService.removeById(id)){
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR,"删除失败");
        }
        return ResponseResult.ok(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 查询所有频道信息
     * @return
     */
    @GetMapping("channels")
    public ResponseResult<List<AdChannel>> findAll(){

        return ResponseResult.ok(adchannelService.list());
    }
}
