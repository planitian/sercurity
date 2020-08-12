package com.example.sercurity.config;

import com.example.sercurity.component.MyFilter;
import com.example.sercurity.component.RestAuthenticationEntryPoint;
import com.example.sercurity.component.RestfulAccessDeniedHandler;
import com.example.sercurity.config.sercurity.*;
import com.example.sercurity.service.PermissonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 配置token登录的
 *
 * @Author: plani
 * 创建时间: 2019/8/16 17:50
 */
@Configuration
@EnableWebSecurity
//启用 方法鉴权
//下面这个注解 ，项目只能有一个类可以使用，要注意
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Order(3)
public class TokenSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(FormSecurityConfig.class);
    @Autowired
    private PermissonService permissonService;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MyAuthticationProvider myAuthticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //requestMatchers 允许指定将在哪个HttpServletRequest实例上调用此HttpSecurity
        //和多httpSecurity 一样的效果
        //就是 当访问 /re/** 这些接口的时候  是使用下面 一部分代码的，链式调用 结束 为 分隔符
        httpSecurity.requestMatchers().antMatchers("/re/**")
                .and()
                .requestMatchers()
                .antMatchers("/re/s")//只要链式 没有结束， 那么这一整个配置 就是一体的
                .and()
                //所有人 都可以访问
                .authorizeRequests().anyRequest().permitAll();


        //其他的url  使用下面的这部分配置
        httpSecurity.csrf().disable()//跨域攻击去除
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()//返回SecurityBuilder
                .authorizeRequests()//配置url
                //这里应该考虑 放开 你的注册接口， 登录接口默认是 /login
                //这里的antMatchers  是在 Filter Chain 最后的Filter起效果。所以 不用放开 /login
                .antMatchers("/register")
                .permitAll()////允许所有人访问
                .anyRequest()//所有请求
                .authenticated()//授权的用户

                //引入了ObjectPostProcessor的概念，可以使用它修改或替换Java配置创建的许多对象实例
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        //设置权限来源  默认的实现类ExpressionBasedFilterInvocationSecurityMetadataSource
                        object.setSecurityMetadataSource(new MySecurityMetadataSource());
                        //设置自定义的 权限 管理器  这里 图方便  直接new了  默认的实现类AffirmativeBased
                        object.setAccessDecisionManager(new MyAccessDecisionManager());
                        return object;
                    }
                })
        ;

        httpSecurity
                //Filter Chain 里面的UsernamePasswordAuthenticationFilter类的位置  添加  我们自定义的Filter
                //在指定Filter类的位置添加筛选器,要注意 位置，这个不要求是Sercurity的 Filter的子类 实例
                .addFilterAt(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
               //spring sercurity会自动的拿你的自定义类，来替换掉自己的默认类，例如，UsernamePasswordAuthenticationFilter 就不会出现在filter chain里面了

                //在 UsernamePasswordAuthenticationFilter 的位置前面加入 Filter
//                .addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)

                //添加一个Filter，该Filter必须是安全框架中提供的Filter的实例或扩展其中一个Filter。该方法确保自动处理Filter的排序。
//                .addFilter(jwtLoginFilter())

                .addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
                //这个 MyFilter
                .addFilterAt(new MyFilter(), LogoutFilter.class)
                //也可以在这里 设置 自定义的 securityInterceptor
                //，FilterSecurityInterceptor 依然会存在
                .addFilterAfter(myFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
        ;

        //配置 全局异常 处理方案
        httpSecurity.exceptionHandling()
                //没有权限访问 时候  返回信息
                .accessDeniedHandler(restfulAccessDeniedHandler)
                //当未登录或者token失效访问接口时，自定义的返回结果
                .authenticationEntryPoint(restAuthenticationEntryPoint);

    }

    /**
     * 在这里 配置  WebSecurity的一些东西
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //设置使用自己的  securityInterceptor
//        web.securityInterceptor(myFilterSecurityInterceptor())
        //忽略 one 路径
        web.ignoring().mvcMatchers("/one");
        System.out.println("hhahaha");
        super.configure(web);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth    // 设置UserDetailsService
                .userDetailsService(userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder);
//                .and()
        //使用 自己的 authenticationProvider  进行具体的认证过称
//                .authenticationProvider(myAuthticationProvider);
    }


    @Bean
    //这种用 @Bean的 并不是 唯一的，如果 你想 ，你可以直接在上面使用的地方  new一个，不过 类里面的一些其他组件 就没法注入了
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JWTAuthenticationFilter(authenticationManager());
    }

    @Bean
    public JWTLoginFilter jwtLoginFilter() throws Exception {
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter();
        jwtLoginFilter.setAuthenticationManager(authenticationManagerBean());
          //设置登录 接口
        jwtLoginFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        return jwtLoginFilter;
    }

    @Bean
    //这个不能替换 FilterSecurityInterceptor ，即这个和FilterSecurityInterceptor是可以共存的
    public MyFilterSecurityInterceptor myFilterSecurityInterceptor() {
        MyFilterSecurityInterceptor myFilterSecurityInterceptor = new MyFilterSecurityInterceptor();
        //设置自定义的 权限 管理器  这里 图方便  直接new了  默认的实现类AffirmativeBased
        myFilterSecurityInterceptor.setAccessDecisionManager(new MyAccessDecisionManager());
        //设置权限来源  默认的实现类ExpressionBasedFilterInvocationSecurityMetadataSource
        myFilterSecurityInterceptor.setSecurityMetadataSource(new MySecurityMetadataSource());
        return myFilterSecurityInterceptor;
    }


}
