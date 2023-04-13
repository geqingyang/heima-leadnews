package com.heima.file.config;

import com.heima.file.service.MinioService;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MinioService.class)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    @Autowired
    private MinioProperties prop;

    @Bean
    public MinioClient buildMinioClient(){
        System.out.println(prop);
        return MinioClient
                .builder()
                .credentials(prop.getAccessKey(),prop.getSecretKey())
                .endpoint(prop.getEndpoint())
                .build();
    }
}
