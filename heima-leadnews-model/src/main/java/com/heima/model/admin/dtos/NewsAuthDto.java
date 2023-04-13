package com.heima.model.admin.dtos;

import com.heima.common.dtos.PageRequestDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewsAuthDto extends PageRequestDto {

    /**
     * 文章标题
     */
    private String title;

    @NotBlank
    private Integer id;

    /**
     * 失败原因
     */
    private String msg;
}