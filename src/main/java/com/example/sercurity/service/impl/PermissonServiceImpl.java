package com.example.sercurity.service.impl;

import com.example.sercurity.dao.RoleMapper;
import com.example.sercurity.dao.UserMapper;
import com.example.sercurity.entity.Permission;
import com.example.sercurity.entity.Role;
import com.example.sercurity.entity.User;
import com.example.sercurity.service.PermissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: plani
 * 创建时间: 2019/8/19 9:20
 */
@Service
public class PermissonServiceImpl implements PermissonService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<Permission> permissionByUser(User user) {
        return null;
    }

    @Override
    public List<Role> rolesByUser(User user) {
        return roleMapper.rolesByUser(user);
    }

    @Override
    public User getUserByUserName(String userName) {
        User user = userMapper.getUserByUserName(userName);
        return user;
    }
}
