package com.example.sercurity.config;

import com.example.sercurity.component.MyFilter;
import com.example.sercurity.component.RestAuthenticationEntryPoint;
import com.example.sercurity.component.RestfulAccessDeniedHandler;
import com.example.sercurity.config.sercurity.JWTAuthenticationFilter;
import com.example.sercurity.config.sercurity.JWTLoginFilter;
import com.example.sercurity.config.sercurity.MyAuthticationProvider;
import com.example.sercurity.service.PermissonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
                .authenticated();//授权的用户

        httpSecurity
                //Filter Chain 里面的UsernamePasswordAuthenticationFilter类的位置  添加  我们自定义的Filter
                //在指定Filter类的位置添加筛选器,要注意 位置，这个不要求是Sercurity的 Filter 实例
                .addFilterAt(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)

                //在 UsernamePasswordAuthenticationFilter 的位置前面加入 Filter
//                .addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)

                //添加一个Filter，该Filter必须是安全框架中提供的Filter的实例或扩展其中一个Filter。该方法确保自动处理Filter的排序。
//                .addFilter(jwtLoginFilter())

                .addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
                //这个 MyFilter
                .addFilterAt(new MyFilter(), LogoutFilter.class);

        //配置 全局异常 处理方案
        httpSecurity.exceptionHandling()
                //没有权限访问 时候  返回信息
                .accessDeniedHandler(restfulAccessDeniedHandler)
                //当未登录或者token失效访问接口时，自定义的返回结果
                .authenticationEntryPoint(restAuthenticationEntryPoint);
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
        return jwtLoginFilter;
    }

}
