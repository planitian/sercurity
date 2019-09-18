package com.example.sercurity.config;

import com.example.sercurity.bo.SecurityUser;
import com.example.sercurity.component.MyFilter;
import com.example.sercurity.component.RestAuthenticationEntryPoint;
import com.example.sercurity.component.RestfulAccessDeniedHandler;
import com.example.sercurity.config.sercurity.JWTAuthenticationFilter;
import com.example.sercurity.config.sercurity.JWTLoginFilter;
import com.example.sercurity.config.sercurity.MyAccessDecisionManager;
import com.example.sercurity.config.sercurity.MyAuthticationProvider;
import com.example.sercurity.entity.User;
import com.example.sercurity.service.PermissonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: plani
 * 创建时间: 2019/8/16 17:50
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);
    @Autowired
    private PermissonService permissonService;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MyAuthticationProvider myAuthticationProvider;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
 /*       httpSecurity.csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")//用户没有登录的话，登录页面
                .usernameParameter("userName")
                .passwordParameter("password")
                .failureUrl("/login?error")//这个是验证密码错误，以后要跳转的页面
                .permitAll()
                .and()
        .authorizeRequests();*/
//        httpSecurity.authorizeRequests().anyRequest().permitAll();
        httpSecurity.csrf().disable()//跨域攻击去除
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()//返回SecurityBuilder
                .authorizeRequests()//配置url
                .antMatchers("/register")//注册接口，任何人都可以访问
                .permitAll()////允许所有人访问
                .anyRequest()//所有请求
                .authenticated()//授权的用户
                ;
        httpSecurity.exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler).authenticationEntryPoint(restAuthenticationEntryPoint);
        httpSecurity
                .addFilterAt(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        httpSecurity.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setAccessDecisionManager(new MyAccessDecisionManager());
                return object;
            }
        });

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth    // 设置UserDetailsService
                .userDetailsService(userDetailsService())
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
//                .and()
//                .authenticationProvider(myAuthticationProvider);
    }

    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {//密码加密
        return new BCryptPasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.equals(encodedPassword);
            }

        };
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new MyAccessDecisionManager();
    }



    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                logger.info("username "+username);
                User user = permissonService.getUserByUserName(username);
                if (user != null) {
                    return new SecurityUser(user, permissonService);
                }
                throw new UsernameNotFoundException("没有用户");
            }
        };
    }



    @Bean
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
