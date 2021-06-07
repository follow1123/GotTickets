package com.yang.gotTickets.service.impl;

import com.yang.gotTickets.service.SpringIocService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @auther YF
 * @create 2021-06-07-10:57
 */
@Service
@Setter(onMethod_ = {@Autowired})
public class SpringIocServiceImpl implements SpringIocService {

    private ApplicationContext applicationContext;

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void injection() {
        System.out.println(beanFactory);

//        Set<Map.Entry<String, String>> entries = error.entrySet();
//        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> next = iterator.next();
//            RootBeanDefinition beanDefinition = new RootBeanDefinition(ErrorCode.class);
//            ConstructorArgumentValues constructor = new ConstructorArgumentValues();
//            constructor.addIndexedArgumentValue(0, next.getKey());
//            constructor.addIndexedArgumentValue(1, next.getValue());
//            beanDefinition.setConstructorArgumentValues(constructor);
//            beanFactory.registerBeanDefinition(next.getKey(), beanDefinition);
//        }
    }
}
