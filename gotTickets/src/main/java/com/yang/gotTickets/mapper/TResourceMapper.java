package com.yang.gotTickets.mapper;

import com.yang.gotTickets.bean.TResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
public interface TResourceMapper extends BaseMapper<TResource> {

    @Select("TRUNCATE `t_resource`")
    int reset();
}
