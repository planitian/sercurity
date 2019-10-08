package com.example.sercurity.config.sercurity;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * 执行具体 鉴权过称的自定义类
 */
public class MyAccessDecisionManager implements AccessDecisionManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param authentication   认证对象 就是前面认证过称  返回了Authentication， 如果你返回的是自定义的 ，那么这里的具体类型就是你自定义的类
     * @param object           spring sercurity使用cglib 代理的controller类的方法 返回的对象
     *                         在FilterSecurityInterceptor中，decide的参数object类型为FilterInvocation，
     *                         MethodSecurityInterceptor中object类型为MethodInvocation，具体类型是ReflectiveMethodInvocation
     *                         如果你自定义的话，这里的object类型 可以变成 你自己定义的类型
     * @param configAttributes SecurityMetadataSource返回的 关于这个object的权限集合
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (object instanceof FilterInvocation) {
            logger.info("在FilterSecurityInterceptor 类型调用");
        } else if (object instanceof MethodInvocation) {
            logger.info("MethodSecurityInterceptor 类型调用");
        } else {
            logger.info("自定义的类型调用");
        }

        //写你自己的鉴权 逻辑 ，不通过的话，直接抛异常就行 AccessDeniedException

    }

    /**
     * 指示此AccessDecisionManager是否能够处理使用传递的ConfigAttribute表示的授权请求。
     *
     * @param attribute
     * @return
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * 指示AccessDecisionManager实现是否能够为指定的受保护对象类型提供访问控制决策。
     *
     * @param clazz 就是
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        //FilterInvocation  MethodInvocation
        logger.info("MyAccessDecisionManager " + clazz.getSimpleName());
        return true;
    }
}
