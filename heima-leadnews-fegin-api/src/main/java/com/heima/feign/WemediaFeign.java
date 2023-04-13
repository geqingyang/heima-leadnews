package com.heima.feign;

import com.heima.model.media.dtos.WmUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 自媒体的openfeign接口
 */
@FeignClient("leadnews-wemedia")
public interface WemediaFeign {


    /**
     * 保存自媒体用户信息
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/wmuser/save")
    Integer saveWmUser(@RequestBody WmUserDto dto);

}