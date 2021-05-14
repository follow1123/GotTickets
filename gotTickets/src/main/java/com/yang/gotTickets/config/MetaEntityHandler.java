package com.yang.gotTickets.config;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.gotTickets.bean.BaseEntity;
import com.yang.gotTickets.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @auther YF
 * @create 2021-04-21-9:04
 */
//@Component
//@Slf4j
public class MetaEntityHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        System.err.println(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.err.println(metaObject.getGetterNames().length + "------ ");

        for (String getterName : metaObject.getGetterNames()) {
            System.err.println(getterName);
        }
    }
}
