package com.yang.gotTickets.util.javassistUtils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * @auther YF
 * @create 2021-06-07-15:09
 */
public class CtBuilder {

    private ClassPool pool;

    public CtBuilder() {
        this.pool = ClassPool.getDefault();
    }

    protected CtClass toCtClass(Class<?> clz) {
        try {
            return pool.get(clz.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected String getNeighborPackage(Class<?> neighbor) {
        String name = neighbor.getName();
        return name.substring(0, name.lastIndexOf("."));
    }
}
