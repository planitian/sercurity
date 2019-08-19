package com.example.sercurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.sercurity.dao")
public class SercurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SercurityApplication.class, args);
    }

}
