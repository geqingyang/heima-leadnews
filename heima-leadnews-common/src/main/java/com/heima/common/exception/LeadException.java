package com.heima.common.exception;


import com.heima.common.enums.AppHttpCodeEnum;
import lombok.Data;

import com.heima.common.enums.AppHttpCodeEnum;
import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class LeadException extends RuntimeException {

    private AppHttpCodeEnum enums;

    public LeadException(String message) {
        super(message);
    }

    public LeadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LeadException(AppHttpCodeEnum enums, String message) {
        super(message);
        this.enums = enums;
    }

    public LeadException(AppHttpCodeEnum enums) {
        this.enums = enums;
    }
}