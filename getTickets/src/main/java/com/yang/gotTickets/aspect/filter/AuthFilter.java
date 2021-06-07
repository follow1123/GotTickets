package com.yang.gotTickets.aspect.filter;

import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.filter.authFilter.base.RoleFilter;
import com.yang.gotTickets.util.AuthUtil;
import com.yang.gotTickets.util.SecurityContextUtil;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

/**
 * @auther YF
 * @create 2021-05-24-15:43
 */
@Aspect
@Component
@Setter(onMethod_ = {@Autowired})
public class AuthFilter {


    private RoleFilter roleFilter;

    @Pointcut("@annotation(com.yang.gotTickets.annotation.authority.AuthFilter)")
    public void authFilter() {
    }

    @Around("authFilter()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Method method =  ((MethodSignature)joinPoint.getSignature()).getMethod();
        try {
            Object[] args = roleFilter.preFilter(method, joinPoint.getArgs());
            if (args == null){
                args = joinPoint.getArgs();
            }
            return roleFilter.sufFilter(method, joinPoint.proceed(args));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


}
