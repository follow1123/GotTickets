package com.yang.gotTickets.service.impl;

import cn.hutool.core.convert.Convert;
import com.yang.gotTickets.bean.TUser;
import com.yang.gotTickets.bean.dto.LoginUserDetailsDto;
import com.yang.gotTickets.exception.BusException;
import com.yang.gotTickets.mapper.TUserMapper;
import com.yang.gotTickets.service.TUserService;
import com.yang.gotTickets.util.JwtUtil;
import com.yang.gotTickets.util.constant.CAuth;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@Service
@Setter(onMethod_ = {@Autowired})
public class TUserServiceImpl extends BaseServiceImpl<TUserMapper, TUser> implements TUserService {


    private UserDetailsService userDetailsService;

    private JwtUtil jwtUtil;

    @Override
    public LoginUserDetailsDto login(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!password.equals(userDetails.getPassword()))
            throw new BusException("用户名或密码错误");
        LoginUserDetailsDto convert = Convert.convert(LoginUserDetailsDto.class, userDetails);
        String token = jwtUtil.createToken(new HashMap<String, Object>() {{
            put(CAuth.JWT_USERNAME_KEY, convert.getUsername());
            put(CAuth.JWT_AUTHS_KEY, convert.getRole().getAuthCode());
        }});
        convert.setToken(token);
        return convert;
    }
}
