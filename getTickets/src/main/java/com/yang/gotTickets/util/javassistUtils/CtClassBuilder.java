package com.yang.gotTickets.util.javassistUtils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.sun.org.apache.xalan.internal.xsltc.dom.NthIterator;
import javassist.*;
import lombok.Data;

import javax.xml.soap.SAAJResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @auther YF
 * @create 2021-06-07-11:27
 */
public class CtClassBuilder extends CtBuilder {

    private CtClass ctClass;

    private final ClassPool pool;

    private CtFiledBuilder ctFiledBuilder;

    private CtMethodBuilder ctMethodBuilder;


    public static CtClassBuilder builder() {
        return new CtClassBuilder();
    }

    CtClassBuilder() {
        pool = ClassPool.getDefault();
    }

    public CtClassBuilder setName(String name, String packageName){
        Assert.isNull(this.ctClass, "类名只能设置一次");
        this.ctClass = pool.makeClass(StrUtil.join(".", name, packageName));
        return this;
    }

    public CtClassBuilder setName(String name, Class<?> neighbor){
        return setName(name, getNeighborPackage(neighbor));
    }


    public CtClassBuilder addField(Consumer<CtFiledBuilder> consumer){
        Assert.notNull(this.ctClass, "请先设置类的名称");
        consumer.accept(new CtFiledBuilder(this.ctClass));
        return this;
    }

    public CtClassBuilder addMethod(Consumer<CtMethodBuilder> consumer){
        Assert.notNull(this.ctClass, "请先设置类的名称");
        consumer.accept(new CtMethodBuilder(this.ctClass));
        return this;
    }


    public CtClass build(){
        return null;
    }

}
