package com.heima.search.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置序列化 long转string
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer , InitializingBean {

        @Autowired(required = false)
        private ObjectMapper obj;

        private SimpleModule getSimpleModule() {
            /**
             * 序列换成Json时,将所有的Long变成String
             * 因为js中得数字类型不能包括所有的java Long值
             */
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

            return simpleModule;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            if (obj != null) {
                SimpleModule simpleModule = getSimpleModule();
                obj.registerModule(simpleModule);
            }
        }

}