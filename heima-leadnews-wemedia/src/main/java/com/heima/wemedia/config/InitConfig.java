package com.heima.wemedia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.heima.common.aliyun","com.heima.common.knife4j"
        ,"com.heima.common.exception", "com.heima.common.bcrypt",
        "com.heima.common.threadpool", "com.heima.common.tess4j",
        "com.heima.common.delayTask"})
public class InitConfig {
}