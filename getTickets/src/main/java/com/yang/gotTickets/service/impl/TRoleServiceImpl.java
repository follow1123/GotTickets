package com.yang.gotTickets.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.gotTickets.annotation.cache.CacheInterrupter;
import com.yang.gotTickets.annotation.cache.Cached;
import com.yang.gotTickets.bean.table.TRole;
import com.yang.gotTickets.exception.BusException;
import com.yang.gotTickets.mapper.TRoleMapper;
import com.yang.gotTickets.service.TRoleService;
import com.yang.gotTickets.service.impl.BaseServiceImpl;
import com.yang.gotTickets.util.AuthUtil;
import com.yang.gotTickets.util.CusEncoder;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sun.security.util.AuthResources_ja;

import java.math.BigInteger;
import java.sql.Wrapper;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.notNull;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author YF
 * @since 2021-05-17
 */
@Service
@Setter(onMethod_ = {@Autowired})
public class TRoleServiceImpl extends BaseServiceImpl<TRoleMapper, TRole> implements TRoleService {

    private AuthUtil authUtil;

    private TRoleMapper roleMapper;

    private String getNextRoleCodeOrDefault(){
        String maxRoleCode = roleMapper.getMaxRoleCode();
        return StrUtil.isBlank(maxRoleCode) ? authUtil.getFirstCode() : authUtil.getNextCode(maxRoleCode);
    }

    @Override
//    @CacheInterrupter
    public boolean updateRoleCodeById(Long id) {
        TRole role = getById(id);
        notNull(role, "用户不存在");

        role.setCode(getNextRoleCodeOrDefault());
        return updateById(role);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
//    @Cached(interruptBy = TRoleServiceImpl.class, methodName = "updateRoleCodeById")
    public List<TRole> listRole() {
        return list();
    }

    @Override
    public boolean insertRole(String name, String description) {
        TRole role = TRole.builder()
                .name(name)
                .description(description)
                .code(getNextRoleCodeOrDefault())
                .build();
        return save(role);
    }
}
