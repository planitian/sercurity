package com.example.sercurity.config.sercurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import java.io.IOException;

//继承AbstractSecurityInterceptor  还有实现 Filter接口
//当然 也可以继承具体的实现类  FilterSecurityInterceptor
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SecurityMetadataSource securityMetadataSource;

    public SecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setSecurityMetadataSource(SecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        logger.info("invoke");
        if (fi.getRequest() != null) {
            // filter already applied to this request and user wants us to observe
            // once-per-request handling, so don't re-do security checking
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            InterceptorStatusToken token = super.beforeInvocation(fi);
            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            } finally {
                super.finallyInvocation(token);
            }
            super.afterInvocation(token, null);
        }

    }

    //重写AbstractSecurityInterceptor的这个方法
    //在FilterSecurityInterceptor 中， beforeInvocation的参数 object类型 为FilterInvocation，
    //这个要和MethodSecurityInterceptor 的MethodInvocation ，具体类型是ReflectiveMethodInvocation
    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        //object 类型是FilterInvocation 就是上面的fi 参数，可以获得 request  response  url等
        logger.info("这里 我没有改动任何东西 感觉默认的就能实现我的功能，有需要的改");
        return super.beforeInvocation(object);
    }

    @Override
    public Class<?> getSecureObjectClass() {
        //这里的 Class 类型 就是beforeInvocation 参数 object的类型
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return securityMetadataSource;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher("/path");
        //判断 是否匹配  没有作用，只是写着玩的
        antPathRequestMatcher.matches(fi.getRequest());
        invoke(fi);
    }
}
