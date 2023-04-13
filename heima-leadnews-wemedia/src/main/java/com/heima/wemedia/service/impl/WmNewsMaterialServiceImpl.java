package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.wemedia.entity.WmNewsMaterial;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsMaterialService;
import org.springframework.stereotype.Service;

@Service
public class WmNewsMaterialServiceImpl extends ServiceImpl<WmNewsMaterialMapper, WmNewsMaterial>
            implements WmNewsMaterialService {
    @Override
    public void deleteByWmNewsId(Integer id) {
        LambdaQueryWrapper<WmNewsMaterial> qw = new LambdaQueryWrapper<>();
        qw.eq(WmNewsMaterial::getNewsId,id);
        this.remove(qw);
    }
}
