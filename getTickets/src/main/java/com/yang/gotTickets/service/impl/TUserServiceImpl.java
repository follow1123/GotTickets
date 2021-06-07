package com.yang.gotTickets.service.impl;

import com.yang.gotTickets.bean.dto.MyUserDetails;
import com.yang.gotTickets.bean.table.TRole;
import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.exception.BusException;
import com.yang.gotTickets.mapper.TUserMapper;
import com.yang.gotTickets.service.TRoleService;
import com.yang.gotTickets.service.TUserService;
import com.yang.gotTickets.util.CusEncoder;
import com.yang.gotTickets.util.JwtUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yang.gotTickets.util.constant.CAuth.*;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
@Service
@Setter(onMethod_ = {@Autowired})
public class TUserServiceImpl extends BaseServiceImpl<TUserMapper, TUser> implements TUserService {


    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    private TRoleService roleService;

    private CusEncoder cusEncoder;

    private JwtUtil jwtUtil;

    @Override
    public Map<String, String> login(String username, String password) {
        HashMap<String, String> resultMap = new HashMap<>();
        // 根据用户名获取用户详情
        MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
        // 准备jwt map
        Map<String, Object> tokenMap = new HashMap<>();
        // 比对密码
        if (!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("用户名或密码错误");
        // 填充jwt map
        tokenMap.put(JWT_USER_KEY, userDetails.getId());
        tokenMap.put(JWT_ROLE_KEY, userDetails.getRoles());
        tokenMap.put(JWT_AUTHS_KEY, userDetails.getAuths());
        // 返回创建的token
        resultMap.put(AUTH_HEADER, AUTH_HEADER_NAME);
        resultMap.put(AUTH_TOKEN, jwtUtil.createToken(tokenMap));
        return resultMap;
}

    @Override
    public boolean allocRole(Long userId, List<Long> roleIds) {
        TUser user = getById(userId);
        if (user == null) throw new BusException("用户不存在");
        List<TRole> roles = roleService.listByIds(roleIds);
        String roleCodes = "";
        for (TRole role : roles) {
            roleCodes = cusEncoder.add(roleCodes, role.getCode());
        }
        user.setRoleCode(roleCodes);
        return updateById(user);
    }
}
