package com.example.sercurity.config;

import com.example.sercurity.config.sercurity.MyAccessDecisionManager;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @Author: plani
 * 创建时间: 2019/9/17 16:23
 */

//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SercurityGlobal extends GlobalMethodSecurityConfiguration {

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        return new MyAccessDecisionManager();
    }

}
