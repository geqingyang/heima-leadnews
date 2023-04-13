package com.heima.model.media.dtos;

import com.heima.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmMaterialDto extends PageRequestDto {
    /**
     * 收藏的
     */
   private Integer isCollection;
}