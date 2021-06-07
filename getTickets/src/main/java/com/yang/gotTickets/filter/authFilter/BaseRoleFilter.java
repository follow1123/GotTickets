package com.yang.gotTickets.filter.authFilter;

import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.filter.authFilter.base.AbsRoleFilter;
import com.yang.gotTickets.util.AuthUtil;
import com.yang.gotTickets.util.SecurityContextUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @auther YF
 * @create 2021-05-26-11:23
 */
@Component
@Setter(onMethod_ = {@Autowired})
public class BaseRoleFilter extends AbsRoleFilter {

    private SecurityContextUtil contextUtil;

    private AuthUtil authUtil;

    @Override
    public Object getMark() {
        Optional<TUser> user = contextUtil.getUser();
        String roleCode = user.map(TUser::getRoleCode).orElse(null);
        return authUtil.getAuths(roleCode);
    }
}
