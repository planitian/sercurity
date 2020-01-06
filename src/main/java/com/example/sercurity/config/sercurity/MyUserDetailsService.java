package com.example.sercurity.config.sercurity;

import com.example.sercurity.bo.SecurityUser;
import com.example.sercurity.entity.Role;
import com.example.sercurity.entity.User;
import com.example.sercurity.service.PermissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private PermissonService permissonService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = permissonService.getUserByUserName(username);
        List<Role> roles = permissonService.rolesByUser(user);
        if (user != null) {
            return new SecurityUser(user, roles);
        }
        throw new UsernameNotFoundException("没有用户");
    }
}
