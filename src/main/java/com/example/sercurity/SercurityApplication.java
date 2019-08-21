package com.example.sercurity;

import com.example.sercurity.utils.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = "com.example.sercurity.dao")
public class SercurityApplication {

    public static void main(String[] args) {
    SpringApplication.run(SercurityApplication.class, args);
    }

}
