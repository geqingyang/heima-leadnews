package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dtos.PageResult;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.wemedia.entity.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {
    Object uploadPicture(MultipartFile file);

    PageResult<WmMaterial> findByPage(WmMaterialDto dto);

    WmMaterial findByUrl(String url);
}
