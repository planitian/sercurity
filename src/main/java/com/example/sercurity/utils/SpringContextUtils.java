package com.example.sercurity.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: plani
 * 创建时间: 2019/8/20 16:49
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> t) {
        return context.getBean(t);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       SpringContextUtils.setContext(applicationContext); ;
    }
}
