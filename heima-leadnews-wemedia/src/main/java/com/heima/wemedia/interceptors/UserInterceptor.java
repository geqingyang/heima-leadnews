package com.heima.wemedia.interceptors;

import com.heima.common.threadlocal.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //得到header中的信息
        String userId = request.getHeader("userId");
        if(StringUtils.isNotBlank(userId)){
            //把用户id存入threadloacl中
            UserThreadLocalUtils.setUserId(Integer.valueOf(userId));
            log.info("UserInterceptor设置用户信息到threadlocal中...");
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("清理threadlocal...");
        UserThreadLocalUtils.removeUser();
    }
}
