package com.heima.app.gateway.filter;

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

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        if(path.contains("/api/v1/login/login_auth")){
//            登录请求，放行
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst("token");
        if(StringUtils.isBlank(token)){
            log.error("当前请求没有token，path={}",path);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
//        校验token
        try{
            Payload payload = JwtUtils.getInfoFromToken(token);
            Integer userId = payload.getUserId();
//            放入请求头，传递到微服务
            ServerHttpRequest httpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.set("userId", userId.toString());
            }).build();
            exchange.mutate().request(httpRequest);
            return chain.filter(exchange);
        }catch(Exception e){
            log.error("当前请求token解析错误，path={}",path);
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
