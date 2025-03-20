package com.sorocode.voice_access_be_demo.global;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); // 파일당 최대 10MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(20)); // 요청당 최대 20MB
        return factory.createMultipartConfig();
    }
}
