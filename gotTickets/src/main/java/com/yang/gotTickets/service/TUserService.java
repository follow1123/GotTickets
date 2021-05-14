package com.yang.gotTickets.service;

import com.yang.gotTickets.bean.TUser;
import com.yang.gotTickets.bean.dto.LoginUserDetailsDto;
import com.yang.gotTickets.service.BaseService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
public interface TUserService extends BaseService<TUser> {

    LoginUserDetailsDto login(String username, String password);
}
