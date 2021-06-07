package com.yang.gotTickets.annotation.authority;



import java.lang.annotation.*;

/**
 * @auther YF
 * @create 2021-05-06-12:32
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Authority {
    String[] value() default {};

}
