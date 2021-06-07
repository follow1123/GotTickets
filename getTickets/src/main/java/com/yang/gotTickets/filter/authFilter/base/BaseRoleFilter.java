package com.yang.gotTickets.filter.authFilter.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.gotTickets.exception.BusException;
import com.yang.gotTickets.util.result.R;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Parameter;
import java.util.function.Consumer;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @auther YF
 * @create 2021-05-25-15:44
 */
public class BaseRoleFilter {

    public BaseRoleFilter(AbsRoleFilter roleFilter, Class<?> controllerType, String methodName) {
        if (!controllerType.getPackage().getName().contains("controller"))
            throw new BusException("非controller类不能进行当前操作");
        Method curMethod = ReflectUtil.getMethodByName(controllerType, methodName);
        roleFilter.putPreMap(curMethod, (r, args) -> {
            Map<String, Object> paramDetail = getParamDetail(curMethod, args);
            preFilter(r, paramDetail);
            return paramDetail.values().toArray();
        });
        roleFilter.putSufMap(curMethod, (r, res) -> {
            sufFilter(r, res);
            return res;
        });
    }

    private Map<String, Object> getParamDetail(Method method, Object[] args) {
        Map<String, Object> map = new LinkedHashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            map.put(parameters[i].getName(), args[i]);
        }
        return map;
    }

    private void resetPage(Page<?> page, int oldListSize, int newListSize) {
        if (oldListSize < newListSize) return;
        int removeCount = oldListSize - newListSize;
        page.setTotal(page.getTotal() - removeCount);
        page.setPages((page.getTotal() + (page.getSize() - 1)) / page.getSize());
    }

    protected void pageData(Object result, Consumer<List<?>> consumer) {
        Page<?> p = (Page<?>) ((R<?>) result).getData();
        List<?> rs = p.getRecords();
        int oldSize = rs.size();
        consumer.accept(rs);
        resetPage(p, oldSize, rs.size());
    }

    public void preFilter(Object mark, Map<String, Object> param) {
    }

    public void sufFilter(Object mark, Object result) {
    }

}
