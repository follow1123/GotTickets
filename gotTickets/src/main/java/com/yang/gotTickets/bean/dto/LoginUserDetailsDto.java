package com.yang.gotTickets.bean.dto;

import com.yang.gotTickets.bean.TRole;
import com.yang.gotTickets.bean.TUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @auther YF
 * @create 2021-04-24-18:11
 */
@Data
@Builder
public class LoginUserDetailsDto implements UserDetails {

    @ApiModelProperty(value = "用户详情")
    private TUser detail;

    @ApiModelProperty(value = "角色")
    private TRole role;

    @ApiModelProperty(value = "token信息")
    private String token;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(hidden = true)
    private Set<GrantedAuthority> authorities;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
