package com.heima.model.user.dtos;

import com.heima.common.dtos.PageRequestDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthDto extends PageRequestDto {

    @NotNull
    private Integer id;
    /**
     * 驳回的信息
     */
    private String msg;
    /**
     * 状态
     0 创建中
     1 待审核
     2 审核失败
     9 审核通过
     */
    private Integer status;
}