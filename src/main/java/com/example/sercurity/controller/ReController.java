package com.example.sercurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: plani
 * 创建时间: 2019/10/9 15:16
 */
@RestController
@RequestMapping("/re")
public class ReController {

    @RequestMapping("sss")
    public String test() {
        return "success";
    }
}
