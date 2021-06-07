package com.yang.gotTickets.service;

import com.yang.gotTickets.bean.table.TResource;
import com.yang.gotTickets.service.BaseService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
public interface TResourceService extends BaseService<TResource> {

    List<TResource> initResource(Map<RequestMappingInfo, HandlerMethod> handlerMethods);

}
