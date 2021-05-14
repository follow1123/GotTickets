package com.yang.gotTickets.service.impl;

import com.yang.gotTickets.annotation.Authority;
import com.yang.gotTickets.bean.TResource;
import com.yang.gotTickets.mapper.TResourceMapper;
import com.yang.gotTickets.service.TResourceService;
import com.yang.gotTickets.service.impl.BaseServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@Service
@Setter(onMethod_ = {@Autowired})
public class TResourceServiceImpl extends BaseServiceImpl<TResourceMapper, TResource> implements TResourceService {

    private TResourceMapper resourceMapper;

    @Override
    public boolean reset() {
        return resourceMapper.reset() > 0;
    }
}
