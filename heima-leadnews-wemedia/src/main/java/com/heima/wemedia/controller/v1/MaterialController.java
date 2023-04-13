package com.heima.wemedia.controller.v1;

import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.wemedia.entity.WmMaterial;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
public class MaterialController {
    @Autowired
    private WmMaterialService wmMaterialService;
    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload_picture")
    public ResponseResult<String> uploadPic(@RequestParam(name = "multipartFile") MultipartFile file){
        return ResponseResult.ok(wmMaterialService.uploadPicture(file));
    }

    @PostMapping("/list")
    public PageResult<WmMaterial> findByPage(@RequestBody WmMaterialDto dto){
        return wmMaterialService.findByPage(dto);
    }
}
