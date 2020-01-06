package com.example.sercurity.config.sercurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: plani
 * 创建时间: 2019/8/19 15:44
 */
@Component
public class MyAuthticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取表单用户名
        String username = (String) authentication.getPrincipal();
        // 获取表单用户填写的密码
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String password1 = userDetails.getPassword();
        if (!Objects.equals(password, password1)) {
            throw new BadCredentialsException("用户名或密码不正确");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return usernamePasswordAuthenticationToken;
    }

    @Override
    //判断传入的authentication类型 是不是 这个类 支持的，这里 和JWT的使用的类型相同
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
