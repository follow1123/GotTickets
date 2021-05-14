package com.yang.gotTickets.bean.dto.request;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.collection.ListUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @auther YF
 * @create 2021-04-26-8:35
 */
public interface Fillable {

    <T> T fillBean(Class<T> type);
    <T> T fillBean(Class<T> type, String... ignoreFields);

    default <T> T fillBean(Object source, Class<T> type) {
        return fillBean(source, type, "");
    }

    default <T> T fillBean(Object source, Class<T> type, String... ignoreFields) {
        ArrayList<String> fields = ListUtil.toList(ignoreFields);
        try {
            return BeanUtil.fillBean(type.newInstance(), new ValueProvider<String>() {
                @Override
                public Object value(String key, Type valueType) {
                    return BeanUtil.getFieldValue(source, key);
                }

                @Override
                public boolean containsKey(String key) {
                    if (fields.size() == 0) return true;
                    return !fields.contains(key);
                }
            }, CopyOptions.create().ignoreNullValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
