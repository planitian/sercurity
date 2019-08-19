package com.example.sercurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @Author: plani
 * 创建时间: 2019/8/19 14:25
 */

@Configuration
public class MyWebMVCConfigure implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");
    }
}
