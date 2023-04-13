package com.heima.common.exception;


import com.heima.common.dtos.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常控制器增强
 */
@RestControllerAdvice
@Slf4j
public class ExceptionCatch {

    @ExceptionHandler(Exception.class)  // 捕获异常类型
    public ResponseResult exception(Exception e) {
        // 记录日志
        log.error(e.getMessage());
        // 返回通用异常信息
        return ResponseResult.error(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**
     * 自定义异常处理
     * @param leadException
     * @return
     */
    @ExceptionHandler(LeadException.class)
    public ResponseResult leadExceptionHandler(LeadException leadException) {
        leadException.printStackTrace();

        if(StringUtils.isNotBlank(leadException.getMessage())){
            log.error("catch exception:{}", leadException.getMessage());
            return ResponseResult.error(leadException.getEnums(),leadException.getMessage());

        }else{
            log.error("catch exception:{}", leadException.getEnums().getErrorMessage());
            return ResponseResult.error(leadException.getEnums());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult paramException(MethodArgumentNotValidException e) {
        // 记录日志
        log.error(e.getMessage());
        // 返回通用异常信息
        return ResponseResult.error(AppHttpCodeEnum.PARAM_INVALID, e.getBindingResult().getFieldError().getDefaultMessage());
    }
}