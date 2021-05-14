package com.yang.gotTickets.annotation;

import java.lang.annotation.*;

/**
 * @auther YF
 * @create 2021-04-26-10:20
 */

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NoToken {
}


