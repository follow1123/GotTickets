package com.yang.gotTickets.mapper;

import com.yang.gotTickets.bean.table.TRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author YF
 * @since 2021-05-17
 */
public interface TRoleMapper extends BaseMapper<TRole> {

    @Select("select `code` from t_role where update_time = (select max(update_time) from t_role)")
    String getMaxRoleCode();
}
