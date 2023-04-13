package com.heima.admin.gateway.filter;

import com.heima.common.dtos.Payload;
import com.heima.common.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    /**
     * 网关全局过滤
     * 如果是登录操作，路由到Admin服务进行登录
     * 如果不是登录操作，就需要判断是否携带token
     * 解析token，获取用户信息，判断是否有响应权限，如果有就放行，如果没有就返回认证错误信息
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取当前请求
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //判断是否是登录操作
        String path = request.getURI().getPath();
        //是则放行
        if (path.contains("/login/in")){
            return chain.filter(exchange);
        }
        //不是登录请求,判断是否已登录
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)){
            //token为空,返回401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //校验token
        try {
            Payload payload = JwtUtils.getInfoFromToken(token);
            Integer userId = payload.getUserId();
            //把用户id放入header中
            request.mutate().header("userId",""+userId).build();
        } catch (Exception e) {
            log.error("验证token出现错误！");
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
