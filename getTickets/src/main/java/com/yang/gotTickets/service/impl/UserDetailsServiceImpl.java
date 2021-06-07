package com.yang.gotTickets.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.gotTickets.bean.dto.MyUserDetails;
import com.yang.gotTickets.bean.table.TRole;
import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.service.TRoleService;
import com.yang.gotTickets.service.TUserService;
import com.yang.gotTickets.util.AuthUtil;
import com.yang.gotTickets.util.CusEncoder;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @auther YF
 * @create 2021-05-21-14:41
 */
@Component
@Setter(onMethod_ = {@Autowired})
public class UserDetailsServiceImpl implements UserDetailsService {

    private TUserService userService;

    private TRoleService roleService;

    private AuthUtil authUtil;

    private CusEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库
        TUser user = userService.getOne(Wrappers.lambdaQuery(TUser.class).eq(TUser::getAccountName, username));
        if (user != null) {

            String authCode = "";
            // 根据自定义的编码获取该用户的全部角色
            Set<String> roleCodes = authUtil.getAuths(user.getRoleCode());
            // 查询这些角色所包含的权限
            List<TRole> list = roleService.list(Wrappers.lambdaQuery(TRole.class).in(TRole::getCode, roleCodes));
            // 就自定义的权限相加，组成对应的权限编码
            for (TRole role : list) {
                authCode = encoder.add(authCode, role.getAuthCode());
            }
            // 创建并反回对应的UserDetails类
            return MyUserDetails.builder()
                    .id(user.getId())
                    .username(username)
                    .password(user.getPassword())
                    .auths(authCode)
                    .roles(user.getRoleCode())
                    .build();
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
