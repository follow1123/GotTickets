package com.yang.gotTickets.service.impl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.ListUtil;
import com.yang.gotTickets.annotation.authority.Authority;
import com.yang.gotTickets.annotation.authority.NoToken;
import com.yang.gotTickets.bean.table.TResource;
import com.yang.gotTickets.bean.table.TRole;
import com.yang.gotTickets.mapper.TResourceMapper;
import com.yang.gotTickets.service.TEnumFieldService;
import com.yang.gotTickets.service.TResourceService;
import com.yang.gotTickets.service.TRoleService;
import com.yang.gotTickets.util.AuthUtil;
import com.yang.gotTickets.util.CusEncoder;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.*;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
@Slf4j
@Service
@Setter(onMethod_ = {@Autowired})
public class TResourceServiceImpl extends BaseServiceImpl<TResourceMapper, TResource> implements TResourceService {

    private TResourceMapper resourceMapper;

    private CusEncoder cusEncoder;

    private TRoleService roleService;

    private TEnumFieldService enumFieldService;

    private AuthUtil authUtil;


    @Override
    public List<TResource> initResource(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
        resourceMapper.truncateTable();
        List<TRole> roleList = roleService.list();
        ArrayList<TResource> resources = new ArrayList<>();
        //遍历接口信息
        boolean needUpdate = false;
        for (RequestMappingInfo info : handlerMethods.keySet()) {
            HandlerMethod handlerMethod = handlerMethods.get(info);
            //获取接口对应方法的ApiOperation注解
            ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
            PatternsRequestCondition patternsCondition = info.getPatternsCondition();
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            Set<String> patterns = new HashSet<>();
            if (patternsCondition != null) {
                patterns = patternsCondition.getPatterns();
            }
            boolean isAccess = isNoToken(handlerMethod);
            Set<String> roleCodes = getAllRoleCodes(handlerMethod);
            boolean haveAuth = !roleCodes.isEmpty();
            for (RequestMethod method : methods) {
                for (String pattern : patterns) {
                    TResource build = TResource.builder()
                            .resourceUrl(pattern)
                            .method(getMethod(method))
                            .description(apiOperation != null ? apiOperation.value() : null)
                            .authCode(haveAuth && !isAccess ? authUtil.getNextCode() : null)
                            .build();
                    if (haveAuth || isAccess) build.setHaveAuth(haveAuth);
                    for (String curCode : roleCodes) {
                        for (TRole tRole : roleList) {
                            if (tRole.getCode().equals(curCode)) {
                                if (!needUpdate) needUpdate = true;
                                tRole.setAuthCode(cusEncoder.add(tRole.getAuthCode(), build.getAuthCode()));
                            }
                        }
                    }
                    log.info(build.toString());
                    resources.add(build);
                }
            }
        }
        if (needUpdate) roleService.updateBatchById(roleList);
        saveBatch(resources);
        return resources;
    }

    private byte getMethod(RequestMethod method) {
        for (Map.Entry<Byte, String> entry : enumFieldService.getEnumValueMap("请求方式").entrySet()) {
            if (entry.getValue().equals(method.name())) return entry.getKey();
        }
        return -1;
    }

    private boolean isNoToken(HandlerMethod handlerMethod) {
        //获取接口对应方法的Authority注解
        return AnnotationUtil.getAnnotation(handlerMethod.getBeanType(), NoToken.class) != null ||
                //获取接口对应方法的NoToken注解
                handlerMethod.getMethodAnnotation(NoToken.class) != null;
    }

    private Set<String> getAllRoleCodes(HandlerMethod handlerMethod) {
        //获取controller类的Authority注解
        Authority classAuthority = AnnotationUtil.getAnnotation(handlerMethod.getBeanType(), Authority.class);
        //获取接口对应方法的Authority注解
        Authority methodAuthority = handlerMethod.getMethodAnnotation(Authority.class);
        HashSet<String> roleCodes = new HashSet<>();
        if (classAuthority != null) roleCodes.addAll(ListUtil.toList(classAuthority.value()));
        if (methodAuthority != null) roleCodes.addAll(ListUtil.toList(methodAuthority.value()));
        return roleCodes;
    }
}
