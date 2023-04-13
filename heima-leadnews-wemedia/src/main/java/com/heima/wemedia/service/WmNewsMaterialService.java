package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.wemedia.entity.WmNewsMaterial;

public interface WmNewsMaterialService extends IService<WmNewsMaterial> {
    void deleteByWmNewsId(Integer id);
}
