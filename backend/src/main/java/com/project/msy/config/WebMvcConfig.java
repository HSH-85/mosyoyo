// src/main/java/com/project/msy/config/WebMvcConfig.java
package com.project.msy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 1) 루트
        registry.addViewController("/")
                .setViewName("forward:/index.html");

        // 2) 단일 세그먼트, 확장자 없는(“.” 없는), api 가 아닌 경로
        registry.addViewController("/{path:^(?!api$)(?!.*\\.).+}")
                .setViewName("forward:/index.html");

        // 3) 다중 세그먼트: 첫 세그먼트가 api 아니고 “.” 없는 경우만
        registry.addViewController("/{path:^(?!api$)(?!.*\\.).+}/**")
                .setViewName("forward:/index.html");
    }
}
