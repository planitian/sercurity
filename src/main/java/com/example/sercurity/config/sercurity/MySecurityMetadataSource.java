package com.example.sercurity.config.sercurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 根据request提供具体权限集合的自定义类
 */
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource, SecurityMetadataSource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 这一部分 我没有写 具体的代码，因为 至少公司项目层面没用到，这里 我只说一下用法
     * 最好参照 默认实现类的代码  很棒
     *
     * @param object FilterSecurityInterceptor 时候  object 为FilterInvocation
     *
     *               MethodSecurityInterceptor   时候  为  MethodInvocation，
     *               AbstractMethodSecurityMetadataSource为所有注解的父类，例如  @Secured({"saas_org_basic_joining"})等
     *               会先调用 AbstractMethodSecurityMetadataSource 的getAttributes(Object object)方法 ，通过下面的代码
     *               再调用 具体注解类的 重载方法
     *               Collection<ConfigAttribute> attrs = getAttributes(mi.getMethod(), targetClass);
     *
     *
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //在这里 根据 object 返回 ，返回所要求的 权限集合
        //默认实现类的代码  ，我直接拷过来的。  RequestMatcher 就是 我们前面配置文件  里面定义的 authorizeRequests 哪里的url

      /*  final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }*/

        //这里 我随便 返回一两个
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        allAttributes.add(new ConfigAttribute() {
            @Override
            public String getAttribute() {
                return "custom";
            }
        });
        logger.info("返回 随便定义的  ConfigAttribute 集合 ");
        return allAttributes;
    }

    /**
     * @return 返回全部 ConfigAttribute
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        //FilterInvocation  MethodInvocation
        logger.info("MySecurityMetadataSource " + clazz.getSimpleName());
        return true;
    }
}
