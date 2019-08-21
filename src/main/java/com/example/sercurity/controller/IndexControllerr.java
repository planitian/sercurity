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
@RestController
@RequestMapping("/")
public class IndexControllerr {

    private Logger logger = LoggerFactory.getLogger(IndexControllerr.class);

    public UserDetails getUser() { //为了session从获取用户信息,可以配置如下
        ;
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            logger.info("类型 " + ((UserDetails) auth.getPrincipal()).getUsername());
            return ((UserDetails) auth.getPrincipal());
        }
        return null;
    }

    @RequestMapping("/root")
//    @Secured({"ROLE_admin", "ROLE_normal"})//前面 角色前面必须要加ROLE
//    @PreAuthorize("hasAuthority('admin')")
//    @PreAuthorize("principal.username.equals(#username)")
    @PreAuthorize("ha")
    @ResponseBody
    public String root() {
//        UserDetails userDetails = getUser();
//        logger.info("进来方法:"+userDetails.getUsername());
        return "root";
    }


    @RequestMapping("/norole")
    public String noRole(Model model) {
        UserDetails userDetails = getUser();
        logger.info(userDetails.getUsername());
        model.addAttribute("username", userDetails.getUsername());
        userDetails.getAuthorities().stream().forEach(new Consumer<GrantedAuthority>() {
            @Override
            public void accept(GrantedAuthority grantedAuthority) {
                logger.info("role " + grantedAuthority.getAuthority());
            }
        });
        return "root";
    }

    @RequestMapping("/login")
    public String login() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        return "login";
    }

}
