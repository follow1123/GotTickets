package com.yang.gotTickets.exception;

import cn.hutool.json.JSONUtil;
import com.yang.gotTickets.util.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther YF
 * @create 2021-04-28-16:11
 */
@Slf4j
@Component
public class AuthenticationHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private void handler(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        R<Boolean> data = R.fail();
        if (e instanceof InsufficientAuthenticationException) {
            data.setMessage("身份认证失败");
        } else if (e instanceof AccessDeniedException) {
            data.setCode(403);
            data.setMessage("权限不足");
        } else {
            data.setMessage(e.getMessage());
        }
        log.error(data.getMessage().concat("\t").concat(request.getRequestURL().toString()).concat("\t").concat(e.getMessage()));
        response.getWriter().println(JSONUtil.toJsonStr(data));
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        handler(request,response,e);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        handler(request,response,accessDeniedException);
    }
}
