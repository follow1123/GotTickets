package com.yang.gotTickets.util;

import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.service.TUserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

/**
 * @auther YF
 * @create 2021-05-09-18:31
 */
@Slf4j
@Component
@Setter(onMethod_ = {@Autowired})
public class SecurityContextUtil {

    private TUserService userService;

    private Optional<SecurityContext> getContext(){
        return Optional.of(SecurityContextHolder.getContext());
    }

    public void setAuth(Long userId, Set<GrantedAuthority> auths){
        Optional<SecurityContext> context = getContext();
        context.ifPresent(securityContext -> securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, auths)));
        log.info("保存用户信息到security上下文");
    }

    public Optional<Authentication> getAuth(){
        Optional<SecurityContext> context = getContext();
        return context.map(SecurityContext::getAuthentication);
    }

    public Optional<TUser> getUser(){
        Optional<Authentication> auth = getAuth();
        TUser user = null;
        if (auth.isPresent()){
            try {
                user = userService.getById((Serializable) auth.get().getPrincipal());
            }catch (Exception e){
                log.info("获取用户失败，id："+ auth.get().getPrincipal());
            }
        }
        if (user == null) return Optional.empty();
        return Optional.of(user);
    }

    public boolean haveAuth(){
        return getAuth().isPresent();
    }
}
