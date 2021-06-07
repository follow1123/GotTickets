package com.yang.gotTickets.mapper;

import com.yang.gotTickets.bean.table.TResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
public interface TResourceMapper extends BaseMapper<TResource> {

    @Update("TRUNCATE t_resource")
    int truncateTable();

}
