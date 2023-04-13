package com.heima.model.user.dtos;

import lombok.Data;

@Data
public class AppLoginResultDto {
    private AppLoginDto appLoginDto;
    private String token;
}
