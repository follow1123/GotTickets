package com.yang.gotTickets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.gotTickets.bean.TRole;
import com.yang.gotTickets.bean.TUser;
import com.yang.gotTickets.bean.dto.LoginUserDetailsDto;
import com.yang.gotTickets.mapper.TRoleMapper;
import com.yang.gotTickets.mapper.TUserMapper;
import com.yang.gotTickets.service.TUserService;
import com.yang.gotTickets.util.AuthUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @auther YF
 * @create 2021-05-06-17:21
 */
@Service
@Slf4j
@Setter(onMethod_ = {@Autowired})
public class UserDetailsServiceImpl implements UserDetailsService {

    private TUserMapper userMapper;

    private TRoleMapper roleMapper;

    private AuthUtil authUtil;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<TUser> wrapper = Wrappers.lambdaQuery(TUser.class).eq(TUser::getAccountName, username);
        TUser user = null;
        try {
            user = userMapper.selectOne(wrapper);
        } catch (Exception e) {
            log.error("查询用户异常" + e.getMessage());
        }
        if (user == null)
            throw new UsernameNotFoundException("用户不存在");
        LambdaQueryWrapper<TRole> wrapper1 = Wrappers.lambdaQuery(TRole.class).eq(TRole::getId, user.getRoleId());
        TRole role = roleMapper.selectOne(wrapper1);
        Set<GrantedAuthority> roles = new HashSet<>();
        authUtil.getAuths(new BigInteger(role.getAuthCode(), 16), roles);
        return LoginUserDetailsDto.builder()
                .detail(user)
                .role(role)
                .username(username)
                .password(user.getPassword())
                .authorities(roles)
                .build();
    }
}
