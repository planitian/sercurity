package com.example.sercurity.bo;

import com.example.sercurity.entity.Role;
import com.example.sercurity.entity.User;
import com.example.sercurity.service.PermissonService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: plani
 * 创建时间: 2019/8/16 17:54
 */

public class SecurityUser implements UserDetails {
    private User user;

    private PermissonService permissonService;


    public SecurityUser(User user, PermissonService permissonService) {
        this.user = user;
        this.permissonService = permissonService;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = permissonService.rolesByUser(user);
        List<GrantedAuthority> authorities = null;
        if (roles!=null&&!roles.isEmpty()){
           authorities= roles.stream().map(new Function<Role, GrantedAuthority>() {
                @Override
                public GrantedAuthority apply(Role role) {
                    //用注解时候  hasRole  判断 角色前面 加 "ROLE_"
//                    return new SimpleGrantedAuthority("ROLE_"+role.getRoleName());
                    //hasAuthority  的时候  就不需要加任何前缀
                    return new SimpleGrantedAuthority(role.getRoleName());
                }
            }).collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    //账户是否未过期,过期无法验证
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //指定用户是否解锁,锁定的用户无法进行身份验证
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //指示是否已过期的用户的凭据(密码),过期的凭据防止认证
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否可用 ,禁用的用户不能身份验证
    @Override
    public boolean isEnabled() {
        return true;
    }
}
