package com.yang.gotTickets.filter.authFilter;

import com.yang.gotTickets.bean.table.TResource;
import com.yang.gotTickets.controller.TResourceController;
import com.yang.gotTickets.filter.authFilter.base.BaseRoleFilter;
import com.yang.gotTickets.filter.authFilter.base.AbsRoleFilter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @auther YF
 * @create 2021-05-25-17:10
 */
@Component
public class GetResourceListFilter extends BaseRoleFilter {

    public GetResourceListFilter(AbsRoleFilter roleFilter) {
        super(roleFilter, TResourceController.class, "listResource");
    }

    @Override
    public void preFilter(Object mark, Map<String, Object> param) {
        Integer pageSize = (Integer) param.get("pageSize");
        if (pageSize > 100) {
            param.put("pageSize", 100);
        }
    }

    @Override
    public void sufFilter(Object mark, Object result) {
        pageData(result, rs ->
                rs.removeIf(t -> ((TResource) t).getResourceUrl().contains("t-resource")));
    }
}
