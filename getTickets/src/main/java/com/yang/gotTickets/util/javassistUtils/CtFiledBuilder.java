package com.yang.gotTickets.util.javassistUtils;

import cn.hutool.core.lang.Assert;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;


/**
 * @auther YF
 * @create 2021-06-04-15:11
 */
public class CtFiledBuilder extends CtBuilder {


    private final  CtClass declaring;

    private CtField ctField;

    public CtFiledBuilder(CtClass declaring) {
        this.declaring = declaring;
    }

    public CtFiledBuilder setTypeName(Class<?> fieldType, String fieldName){
        try {
            this.ctField = new CtField(toCtClass(fieldType), fieldName, this.declaring);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CtFiledBuilder setModifier(int modifier){
        Assert.notNull(this.ctField, "请先设置属性的类型和名称");
        this.ctField.setModifiers(modifier);
        return this;
    }

    public void build(){
        Assert.notNull(this.ctField, "请先设置属性的类型和名称");
        try {
            this.declaring.addField(this.ctField);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }


}
