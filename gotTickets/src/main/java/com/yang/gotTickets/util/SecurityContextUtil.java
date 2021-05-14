package com.yang.gotTickets.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * @auther YF
 * @create 2021-05-09-18:31
 */
public class SecurityContextUtil {
    public void setAuth(Set<GrantedAuthority> auths){
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(null, null, auths));
    }

    public Authentication getAuth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean haveAuth(){
        return getAuth() != null;
    }
}
