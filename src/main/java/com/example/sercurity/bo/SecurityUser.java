package com.example.sercurity.bo;

import com.example.sercurity.entity.Role;
import com.example.sercurity.entity.User;
import com.example.sercurity.service.PermissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
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
        String jsonStr = "{\n" +
                "\t0: {\n" +
                "\t\t\"fun_code02企业管理员\": \"Y\",\n" +
                "\t\t\"fun_code03企业管理员\": \"Y\",\n" +
                "\t\t\"fun_code01企业管理员\": \"Y\",\n" +
                "\t\t\"saas_app_launch\": \"Y\",\n" +
                "\t\t\"fun_code04企业用户\": \"Y\"\n" +
                "\t},\n" +
                "\t\"appAdmin\": {},\n" +
                "\t\"saas\": {\n" +
                "\t\t\"saas_manage_org_apply_select\": \"Y\",\n" +
                "\t\t\"saas_org_basic_joining\": \"Y\",\n" +
                "\t\t\"saas_manage_app_change\": \"Y\",\n" +
                "\t\t\"saas_app_home_people_remove\": \"Y\",\n" +
                "\t\t\"saas_manage_account_basic\": \"Y\",\n" +
                "\t\t\"saas_app_home_tree_peopl\": \"Y\",\n" +
                "\t\t\"saas_app_details_applylist\": \"Y\",\n" +
                "\t\t\"saas_app_home_isOrNo_shortcut\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_remove\": \"Y\",\n" +
                "\t\t\"saas_msg_update\": \"Y\",\n" +
                "\t\t\"saas_app_store_apply_app\": \"Y\",\n" +
                "\t\t\"saas_manage_app_linesLimit\": \"Y\",\n" +
                "\t\t\"saas_msg_delete\": \"Y\",\n" +
                "\t\t\"saas_app_home_add\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_get\": \"Y\",\n" +
                "\t\t\"saas_app_home_add_shortcut\": \"Y\",\n" +
                "\t\t\"saas_org_basic_create\": \"Y\",\n" +
                "\t\t\"saas_app_home_people_query\": \"Y\",\n" +
                "\t\t\"saas_org_basic_select_invitation_org\": \"Y\",\n" +
                "\t\t\"saas_app_home_ctrl_app\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_temp_removeall\": \"Y\",\n" +
                "\t\t\"saas_app_home_people_add\": \"Y\",\n" +
                "\t\t\"saas_manage_app_select\": \"Y\",\n" +
                "\t\t\"saas_manage_consume_count\": \"Y\",\n" +
                "\t\t\"saas_manage_group_create\": \"Y\",\n" +
                "\t\t\"saas_app_order_coupon_payable\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_move\": \"Y\",\n" +
                "\t\t\"saas_manage_account_flow\": \"Y\",\n" +
                "\t\t\"saas_org_basic_check_joined\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_select\": \"Y\",\n" +
                "\t\t\"saas_pay_launchFun\": \"Y\",\n" +
                "\t\t\"saas_pay_consumptionPay\": \"Y\",\n" +
                "\t\t\"saas_app_home_appType\": \"Y\",\n" +
                "\t\t\"saas_manage_consume_detail\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_save_import\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_tobe_leader\": \"Y\",\n" +
                "\t\t\"saas_org_basic_select_count\": \"Y\",\n" +
                "\t\t\"saas_org_basic_upload_authorization\": \"Y\",\n" +
                "\t\t\"saas_msg_list\": \"Y\",\n" +
                "\t\t\"saas_pay_payFun\": \"Y\",\n" +
                "\t\t\"saas_app_home_remove_shortcut\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_temp_readcount\": \"Y\",\n" +
                "\t\t\"saas_manage_group_select\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_batch_save\": \"Y\",\n" +
                "\t\t\"saas_app_home_remove\": \"Y\",\n" +
                "\t\t\"saas_manage_User_consumption_list\": \"Y\",\n" +
                "\t\t\"saas_pay_launchApp\": \"Y\",\n" +
                "\t\t\"saas_manage_group_edit\": \"Y\",\n" +
                "\t\t\"saas_manage_app_setLines\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_temp_read\": \"Y\",\n" +
                "\t\t\"saas_manage_org/apply_decide\": \"Y\",\n" +
                "\t\t\"saas_app_store_apply_app_applist\": \"Y\",\n" +
                "\t\t\"saas_app_order_coupon\": \"Y\",\n" +
                "\t\t\"saas_manage_consume_cancel\": \"Y\",\n" +
                "\t\t\"saas_app_home_select\": \"Y\",\n" +
                "\t\t\"saas_manage_group_delete\": \"Y\",\n" +
                "\t\t\"saas_manage_app_adminInfo\": \"Y\",\n" +
                "\t\t\"saas_manage_consume_select\": \"Y\",\n" +
                "\t\t\"saas_pay_freeApp\": \"Y\",\n" +
                "\t\t\"saas_app_details\": \"Y\",\n" +
                "\t\t\"saas_app_home_apply\": \"Y\",\n" +
                "\t\t\"saas_manage_group_member_save\": \"Y\"\n" +
                "\t},\n" +
                "\t\"admin\": \"N\",\n" +
                "\t\"apps\": {\n" +
                "\t\t\"0\": \"Y\",\n" +
                "\t\t\"saas\": \"Y\"\n" +
                "\t}\n" +
                "}\n" +
                "}";
        List<Role> roles = permissonService.rolesByUser(user);
        List<GrantedAuthority> authorities = null;
        if (roles!=null&&!roles.isEmpty()){
           authorities= roles.stream().map(new Function<Role, GrantedAuthority>() {
                @Override
                public GrantedAuthority apply(Role role) {
//                    return new SimpleGrantedAuthority("ROLE_"+role.getRoleName());
                    return new SimpleGrantedAuthority(jsonStr);
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
