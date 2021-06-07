package com.yang.gotTickets.filter.authFilter;

import com.yang.gotTickets.controller.TResourceController;
import com.yang.gotTickets.filter.authFilter.base.BaseRoleFilter;
import com.yang.gotTickets.filter.authFilter.base.AbsRoleFilter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @auther YF
 * @create 2021-05-25-15:57
 */
@Component
public class GetResourceFilter  extends BaseRoleFilter {

    public GetResourceFilter(AbsRoleFilter roleFilter) {
        super(roleFilter, TResourceController.class, "getById");
    }

    @Override
    public void preFilter(Object mark, Map<String, Object> param) {
        System.out.println(mark);
        param.put("id", 2L);
    }

}
