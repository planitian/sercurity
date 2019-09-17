package com.example.sercurity.config;

import com.example.sercurity.config.sercurity.MyAccessDecisionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @Author: plani
 * 创建时间: 2019/9/17 16:23
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SercurityGlobal extends GlobalMethodSecurityConfiguration {

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return new MyAccessDecisionManager();
    }
}
