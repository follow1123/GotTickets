package com.yang.gotTickets.annotation.authority;

import java.lang.annotation.*;

/**
 * @auther YF
 * @create 2021-05-24-15:47
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthFilter {
}
