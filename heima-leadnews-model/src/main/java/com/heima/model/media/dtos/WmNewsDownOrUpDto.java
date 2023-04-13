package com.heima.model.media.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmNewsDownOrUpDto {
    private Long article;
    private Boolean enable;
}
