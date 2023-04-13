package com.heima.common.bcrypt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class PasswordConfig {

    /**
     * 随机秘钥的长度
     */
    private int seedLength = 32;
    /**
     * : 10 # 加密强度4~31，决定了密码和盐加密时的运算次数，超过10以后加密耗时会显著增加
     */
    private Integer strength = 10;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

//      加密前度，数字越大强度越大，越安全，越耗时
        SecureRandom random = new SecureRandom(SecureRandom.getSeed(seedLength));
        return new BCryptPasswordEncoder(strength, random);
    }
}
