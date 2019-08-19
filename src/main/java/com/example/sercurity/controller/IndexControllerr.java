package com.example.sercurity.controller;

import com.example.sercurity.common.CommonResult;
import com.example.sercurity.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: plani
 * 创建时间: 2019/8/19 10:30
 */
@Controller
@RequestMapping("/")
public class IndexControllerr {
    @Autowired
    private UserDetailsService userDetailsService;

    private Logger logger = LoggerFactory.getLogger(IndexControllerr.class);


//    @RequestMapping("/index")
//    public String  index(User user, Model model){
////        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
////        logger.info("我登录啦");
//        return "index";
//    }
    public UserDetails getUser() { //为了session从获取用户信息,可以配置如下
      ;
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails){
            logger.info("类型 "+((UserDetails) auth.getPrincipal()).getUsername());
            return ((UserDetails) auth.getPrincipal());
        }
        return null;
    }
    @RequestMapping("/root")
    @PreAuthorize("hasRole('admin')")
    public String root(){
        UserDetails userDetails = getUser();
        logger.info(userDetails.getUsername());
        return "root";
    }

    @RequestMapping("/norole")
    public String noRole(Model model){
        UserDetails userDetails = getUser();
        logger.info(userDetails.getUsername());
        model.addAttribute("username", userDetails.getUsername());
        return "root";
    }

}
