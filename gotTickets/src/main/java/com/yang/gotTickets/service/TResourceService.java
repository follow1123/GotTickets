package com.yang.gotTickets.service;

import com.yang.gotTickets.bean.TResource;
import com.yang.gotTickets.service.BaseService;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
public interface TResourceService extends BaseService<TResource> {

    boolean reset();

}
