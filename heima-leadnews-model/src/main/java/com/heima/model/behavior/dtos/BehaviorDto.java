package com.heima.model.behavior.dtos;

import lombok.Data;

@Data
public class BehaviorDto {

    private String equipmentId;
    private Integer userId;
    private Long articleId;
    private Integer type;
}
