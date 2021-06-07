package com.yang.gotTickets.annotation.cache;

import java.lang.annotation.*;

/**
 * @auther YF
 * @create 2021-05-27-10:51
 * 缓存主键，将标记改注解的方法执行后的返回值存入redis缓存
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Cached {

    /**
     * 缓存将被该对象内的某个方法清空，与method方法配合使用
     * 对象必须是有CacheInterrupter注解标记
     * @return
     */
    Class<?> interruptBy() default Cached.class;

    /**
     * 标记打断类内的具体某个方法
     * @return
     */
    String methodName() default "";

    /**
     * 默认超时时间4小时
     * @return
     */
    long timeout() default 14400000;
}
