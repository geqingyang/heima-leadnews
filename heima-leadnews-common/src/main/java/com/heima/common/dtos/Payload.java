package com.heima.common.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class Payload {

    private String id;
    private Integer userId;
    private Date expiration;
}
