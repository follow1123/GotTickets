package com.yang.gotTickets.util.javassistUtils;

import cn.hutool.core.lang.Assert;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @auther YF
 * @create 2021-06-07-11:28
 */
public class CtMethodBuilder extends CtBuilder {
    private final CtClass declaring;

    private CtMethod ctMethod;

    public CtMethodBuilder(CtClass declaring) {
        this.declaring = declaring;
    }

    public CtMethodBuilder setMethodInfo(String methodName, Class<?> resultType, Class<?>... paramsType) {
        if (paramsType == null) paramsType = new Class[0];
        CtClass[] paramCtClass = new CtClass[paramsType.length];
        for (int i = 0; i < paramsType.length; i++) {
            paramCtClass[i] = toCtClass(paramsType[i]);
        }
        this.ctMethod = new CtMethod(toCtClass(resultType), methodName, paramCtClass, declaring);
        return this;
    }

    public CtMethodBuilder setModifier(int modifier){
        ctMethod.setModifiers(modifier);
        return this;
    }

    public CtMethodBuilder setBody(Function<Map<String, Object>, Object> function){
        try {
            ctMethod.setBody("");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CtMethodBuilder setBody(Consumer<Map<String, Object>> consumer){
        try {
            ctMethod.setBody("");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CtMethodBuilder setBody(Supplier<Object> supplier){
        try {
            ctMethod.setBody("");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void build() {
        Assert.notNull(this.ctMethod, "请先设置属性的类型和名称");
        try {
            this.declaring.addMethod(this.ctMethod);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
