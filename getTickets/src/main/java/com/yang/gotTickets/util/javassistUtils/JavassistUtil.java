package com.yang.gotTickets.util.javassistUtils;

import cn.hutool.core.lang.Assert;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import lombok.SneakyThrows;

/**
 * @auther YF
 * @create 2021-06-04-15:00
 */
public class JavassistUtil {

    private static final ClassPool pool = ClassPool.getDefault();

    @SneakyThrows
    public static CtClass toCtClass(Class<?> cls) {
        Assert.notNull(cls, "类型不能为空");
        return pool.get(cls.getName());
    }

    public static CtClass toCtClass(Object o){
        Assert.notNull(o, "对象不能为空");
        return toCtClass(o.getClass());
    }

}
