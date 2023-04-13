package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dtos.PageResult;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.dtos.WmNewsPageReqDto;
import com.heima.wemedia.entity.WmNews;

public interface WmNewsService extends IService<WmNews> {
    PageResult<WmNews> findByPage(WmNewsPageReqDto dto);

    void submit(WmNewsDto wmNewsDto);

    void downOrUp(WmNewsDto dto);
}
