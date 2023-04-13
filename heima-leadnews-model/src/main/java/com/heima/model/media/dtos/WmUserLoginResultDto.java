package com.heima.model.media.dtos;

import com.heima.model.admin.dtos.AdUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmUserLoginResultDto {

    private WmUserDto user;

    private String token;

}