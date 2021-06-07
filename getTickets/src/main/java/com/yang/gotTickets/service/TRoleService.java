package com.yang.gotTickets.service;

import com.yang.gotTickets.bean.table.TRole;
import com.yang.gotTickets.service.BaseService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author YF
 * @since 2021-05-17
 */
public interface TRoleService extends BaseService<TRole> {

    boolean updateRoleCodeById(Long id);

    boolean insertRole(String name, String description);

    List<TRole> listRole();
}
