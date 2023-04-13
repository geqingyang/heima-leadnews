package com.heima.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApUserLoginResultDto {
    private ApUser user;
    private String token;
}
