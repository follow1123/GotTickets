package com.yang.gotTickets.service;

import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
public interface TUserService extends BaseService<TUser> {

    Map<String, String> login(String username, String password);

    boolean allocRole(Long userId, List<Long> roleIds);
}
