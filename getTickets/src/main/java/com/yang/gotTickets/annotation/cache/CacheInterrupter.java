package com.yang.gotTickets.annotation.cache;

import java.lang.annotation.*;

/**
 * @auther YF
 * @create 2021-05-27-10:59
 * 标记打断方法对象
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheInterrupter {

}
