package com.yang.gotTickets.service;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @auther YF
 * @create 2021-04-19-15:17
 */
public interface BaseService<T> extends IService<T> {

    Long insertOneGetId(T data);

    boolean insertBatch(Collection<T> datas);

    int insertIfNotExist(T entity, SFunction<T, Object>... nonRepeatFields);

    int insertIfNotExist(T entity, boolean useOr, SFunction<T, Object>... nonRepeatFields);

    boolean exeUseSqlSession(Consumer<SqlSession> fun);
}
