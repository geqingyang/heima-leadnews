package com.heima.user.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.heima.common.knife4j"
        ,"com.heima.common.exception",
        "com.heima.common.bcrypt"})
public class InitConfig {
}
