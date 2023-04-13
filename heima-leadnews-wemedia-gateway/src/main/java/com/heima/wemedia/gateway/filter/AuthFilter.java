package com.heima.wemedia.gateway.filter;

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
    /**
     * 登录校验过滤
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//        获取请求对象
        ServerHttpRequest request = exchange.getRequest();
//        获取响应对象
        ServerHttpResponse response = exchange.getResponse();
//        获取当前的请求路径
        String path = request.getURI().getPath();
        log.info("请求路径path={}",path);
//        判断是否登录请求，如果是直接放行
        if(path.contains("/login/in")){
            return chain.filter(exchange);
        }
//        获取token
        String token = request.getHeaders().getFirst("token");
        log.info("token={}",token);
//        如果没有token，直接返回401，结束
        if(StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
//        验证token，获取payload，
        try {
            Payload payload = JwtUtils.getInfoFromToken(token);
//        获取userId
            Integer userId = payload.getUserId();
            log.info("userId",userId);
//        重置header，放入userId
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.set("userId", userId.toString());
            }).build();
            exchange.mutate().request(serverHttpRequest);
        }catch (Exception e){
            log.info("验证token失败！");
            e.printStackTrace();
//            出现异常返回401，结束
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

//        放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
