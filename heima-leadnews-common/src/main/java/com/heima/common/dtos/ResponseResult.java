package com.heima.common.dtos;

import com.heima.common.enums.AppHttpCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用的结果返回类
 * @param <T>
 */
@Data
public class ResponseResult<T> implements Serializable {

    private String host;

    private Integer code;

    private String errorMessage;

    private T data;

    public ResponseResult() {
        this.code = 0;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.errorMessage = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
    }

    public static ResponseResult ok() {
        return ok(null);
    }

    public static <T> ResponseResult ok(T data) {
        return new ResponseResult(0, data);
    }

    public static ResponseResult error(int code, String msg) {
        return new ResponseResult(code, msg);
    }

    public static ResponseResult error(AppHttpCodeEnum enums) {
        return new ResponseResult(enums.getCode(), enums.getErrorMessage());
    }

    public static ResponseResult error(AppHttpCodeEnum enums, String errorMessage) {
        return new ResponseResult(enums.getCode(), errorMessage);
    }
}
