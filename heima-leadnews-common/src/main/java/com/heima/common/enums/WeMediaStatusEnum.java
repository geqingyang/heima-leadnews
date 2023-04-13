package com.heima.common.enums;

import lombok.Getter;

@Getter
public enum WeMediaStatusEnum {
    WAIT_AUDIT(1),
    FAILURE_AUDIT(2),
    WAIT_PUBLISH(8),
    SUCCESS_AUDIT(9);

    Integer status;
    public Integer getStatus(){
        return status;
    }
    WeMediaStatusEnum(int status) {

    }
}
