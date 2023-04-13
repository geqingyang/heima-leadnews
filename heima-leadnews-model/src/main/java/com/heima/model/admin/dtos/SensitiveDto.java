package com.heima.model.admin.dtos;

import com.heima.common.dtos.PageRequestDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SensitiveDto extends PageRequestDto {

    private Integer id;
    /**
     * 敏感词名称
     */
    @NotBlank(message = "名称不能为空")
    private String sensitives;
}
