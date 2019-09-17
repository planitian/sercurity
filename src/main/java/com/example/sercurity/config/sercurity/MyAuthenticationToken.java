package com.example.sercurity.config.sercurity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author: plani
 * 创建时间: 2019/9/17 17:04
 */
public class MyAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String appId;
    public MyAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MyAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
