package com.heima.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化配置
 */
@Configuration
@ComponentScan({"com.heima.common.knife4j","com.heima.common.exception","com.heima.common.bcrypt"})
public class InitConfig {}
