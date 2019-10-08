package com.example.sercurity.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

//下面这个注解 ，项目只能有一个类可以使用，要注意
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MyGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {


    /**
     * 设置 AccessDecisionManager  具体的权限鉴权  自己的定义的类 ，如果你想定制化
     *
     * @return
     */
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        return super.accessDecisionManager();
    }

    /**
     * 权限 数据来源  返回你自定义的类，如果 你想设置 MethodSecurityMetadataSource
     *
     * @return
     */
    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return super.customMethodSecurityMetadataSource();
    }

}
