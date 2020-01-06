package com.example.sercurity.service;

import com.example.sercurity.entity.Permission;
import com.example.sercurity.entity.Role;
import com.example.sercurity.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: plani
 * 创建时间: 2019/8/16 17:59
 */
public interface PermissonService extends Serializable {
    List<Permission> permissionByUser(User user);

    List<Role> rolesByUser(User user);

    User getUserByUserName(String userName);

}
