package com.example.sercurity.service;

import com.example.sercurity.entity.Permission;
import com.example.sercurity.entity.User;

import java.util.List;

/**
 * @Author: plani
 * 创建时间: 2019/8/16 17:59
 */
public interface permissonService {
    List<Permission> permissionByUser(User user);
}
