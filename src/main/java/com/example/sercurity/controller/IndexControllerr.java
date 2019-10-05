package com.example.sercurity.controller;

import com.example.sercurity.common.CommonResult;
import com.example.sercurity.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * @Author: plani
 * 创建时间: 2019/8/19 10:30
 */
@Controller
@RequestMapping("index/")
public class IndexControllerr {

    private Logger logger = LoggerFactory.getLogger(IndexControllerr.class);


    @RequestMapping("root")
    //要用  root 权限 才可以访问这个接口
    @PreAuthorize("hasAuthority('admin')")
    public String root(Model model) {
        logger.info("我进来了");
        //可以通过 SecurityContextHolder.getContext().getAuthentication() 来得到当前的认证对象
        //Authentication 有很多信息，你可以自己实现一个类，任何在filter中调用SecurityContextHolder.getContext().setAuthentication(); 存储你的认证对象
        //这里得到的有可能是 你自定义的，也有可能是 匿名的认证对象。
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getName());

        return "index";
    }

    //不需要 任何权限都可以访问，但这个不代表 任何人都可以访问这个接口，这个需要看你的设置。
    @RequestMapping("norole")
    public String noRole(Model model) {
        return "norole";
    }

    @RequestMapping("success")
    public String success() {
        return "success";
    }
   //在配置文件里面 放开这个接口 ，也没有配置鉴权相关的注解，所以 访问这个接口没有任何限制
    @RequestMapping("asdf")
    public String asd() {
        System.out.println("ssssssssssssssssssssssss");
        return "asdf";
    }
}
