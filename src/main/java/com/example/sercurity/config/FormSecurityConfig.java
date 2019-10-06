package com.example.sercurity.config;

import com.example.sercurity.bo.SecurityUser;
import com.example.sercurity.component.RestAuthenticationEntryPoint;
import com.example.sercurity.component.RestfulAccessDeniedHandler;
import com.example.sercurity.config.sercurity.MyAuthticationProvider;
import com.example.sercurity.entity.User;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 配置form 登录的
 *
 * @Author: plani
 * 创建时间: 2019/8/16 17:50
 */
@Configuration
@EnableWebSecurity
//启用 方法鉴权  全局 只能有一个类 有这个注解。
/*@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Order(3)//通过这个数字 来决定两个config那个生效*/
public class FormSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(FormSecurityConfig.class);
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
        httpSecurity
                .csrf().disable()//关闭csrf
                .formLogin()//设置表单登录的一些细节，就是对 form表达提交  做了一些封装。完全可以自己直接请求接口
                .loginPage("/login")//用户没有登录的话，登录页面,这个需要你在controller方法里面定义这个，然后 转到
                //处理的url，这个会把filter的拦截url 改成/myaction，没有什么影响，默认是 login
                //如果 这里  你自定义了 url  那么，form 表单的action 就必须是 /myaction
                .loginProcessingUrl("/myaction")
                .usernameParameter("userName")//设定参数名字
                .passwordParameter("password")//设定参数名字
                .failureUrl("/login?error")//这个是验证密码错误，以后要跳转的页面,默认就是 /login?error
                .successForwardUrl("/index/success")//这是一个post转发请求，注意post
                .permitAll()//放开限制，允许任何人访问
                //下面的会覆盖 上面的 successForwardUrl  和 failureUrl  ，使他们不起作用。
                /*       .successHandler((request, response, authentication) -> {
                           //成功认证 以后的 处理。  可以放一些业务需要的逻辑
                           logger.info("认证成功");
                       })
                       .failureHandler((request, response, exception) -> {
                           //认证失败以后的处理
                           logger.info("认证失败");
                       })*/
                .and()
                .logout().logoutUrl("/logout")//退出 url ，默认就是 /logout
                .and()
                .authorizeRequests()//对requset 开启访问控制
                //   index/asdf   可以被任何人访问，访问这个url 如果没有登录的话，也不会跳到登录页面
                .antMatchers("/index/asdf").permitAll()
                //这个匹配器将使用与Spring MVC用于匹配的相同的规则。例如，通常路径“/path”的映射会与“/path”、“/path/”、“/path.html”匹配
                .mvcMatchers("mvc").authenticated()
                //其他的任何 url都开启 认证。 要注意静态资源
                .anyRequest().authenticated()
                .and()
                .httpBasic();//开启httpBasic

        //下面这个配置  会把上面的 loginPage覆盖掉。 form登录时候 就不要用这个了
/*        //配置 全局异常 处理方案
        httpSecurity.exceptionHandling()
                //没有权限访问 时候  返回信息
                .accessDeniedHandler(restfulAccessDeniedHandler)
                //当未登录或者token失效访问接口时，自定义的返回结果
                .authenticationEntryPoint(restAuthenticationEntryPoint);*/


        //使用自己定义的  authenticationProvider
//        httpSecurity.authenticationProvider(myAuthticationProvider);

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth    // 设置UserDetailsService
                .userDetailsService(userDetailsService())
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
//                .and()
        //使用 自己的 authenticationProvider  进行具体的认证过称
//                .authenticationProvider(myAuthticationProvider);
    }


    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {//密码加密
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                logger.info("username " + username);
                User user = permissonService.getUserByUserName(username);
                if (user != null) {
                    return new SecurityUser(user, permissonService);
                }
                throw new UsernameNotFoundException("没有用户");
            }
        };
    }

}
