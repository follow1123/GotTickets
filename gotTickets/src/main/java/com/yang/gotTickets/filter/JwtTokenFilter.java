package com.yang.gotTickets.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.yang.gotTickets.util.AuthUtil;
import com.yang.gotTickets.util.JwtUtil;
import com.yang.gotTickets.util.SecurityContextUtil;
import com.yang.gotTickets.util.constant.CAuth;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @auther YF
 * @create 2021-04-24-23:43
 */
@Component
@Configuration
@Setter(onMethod_ = {@Autowired})
public class JwtTokenFilter extends OncePerRequestFilter {

    private AuthenticationEntryPoint authenticationEntryPoint;

    private UserDetailsService userDetailsService;

    private SecurityContextUtil contextUtil;

    private JwtUtil jwtUtil;

    private AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(CAuth.AUTH_HEADER_NAME);
        if (StrUtil.isNotBlank(token)) {
            HashMap<String, Object> map = new HashMap<>();
            JWTVerificationException exception = jwtUtil.verifyAndGet(token, map);
            if (exception != null) {
                AuthenticationException authException = exception instanceof TokenExpiredException ?
                        new CredentialsExpiredException("登录过期，请重新登录") :
                        new BadCredentialsException("错误的Token");
                authenticationEntryPoint.commence(request, response, authException);
                return;
            }
            Object name = map.get(CAuth.JWT_USERNAME_KEY);
            if (name != null && !contextUtil.haveAuth()) {
                Set<GrantedAuthority> roles = new HashSet<>();
                authUtil.getAuths(new BigInteger(Convert.convert(String.class, map.get(CAuth.JWT_AUTHS_KEY)), 16), roles);
                contextUtil.setAuth(roles);
            }
        }
        filterChain.doFilter(request, response);
    }

}
