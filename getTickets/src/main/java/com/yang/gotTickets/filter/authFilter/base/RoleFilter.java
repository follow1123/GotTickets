package com.yang.gotTickets.filter.authFilter.base;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @auther YF
 * @create 2021-05-25-15:01
 */
public interface RoleFilter {

    Object[] preFilter(Method method, Object[] args);

    Object sufFilter(Method method, Object result);

}
