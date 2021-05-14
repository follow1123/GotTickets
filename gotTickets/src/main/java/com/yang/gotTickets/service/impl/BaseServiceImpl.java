package com.yang.gotTickets.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.gotTickets.bean.BaseEntity;
import com.yang.gotTickets.service.BaseService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @auther YF
 * @create 2021-04-19-15:17
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {


    protected QueryWrapper<T> getEqWrapperByData(T data, String... ignoreFields) {
        QueryWrapper<T> wrapper = new QueryWrapper<T>();
        List<String> ignoreFs = Stream.of(ignoreFields).collect(Collectors.toList());
        Field[] fields = ReflectUtil.getFieldsDirectly(data.getClass(), false);
        for (Field field : fields) {
            TableField ann = field.getAnnotation(TableField.class);
            if (!ignoreFs.contains(field.getName())
                    && (ann == null || ann.exist())
                    && !"serialVersionUID".equals(field.getName())
            ) {
                Object fieldValue = BeanUtil.getFieldValue(data, field.getName());
                wrapper.eq(fieldValue != null, StrUtil.toSymbolCase(field.getName(), '_'), fieldValue);
            }
        }
        return wrapper;
    }

    @Override
    public Long insertOneGetId(T data) {
        boolean b = save(data);
        if (b) {
            return data.getId();
        }
        return null;
    }

    @Override
    public boolean insertBatch(Collection<T> datas) {
        if (datas.size() < 100) {
            return saveBatch(datas);
        }
        return saveBatch(datas, 100);
    }


    protected boolean existOr(T entity, SFunction<T, Object>... nonRepeatFields) {
        if (nonRepeatFields.length == 0) return false;

        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();

        for (int[] i = {0}; i[0] < nonRepeatFields.length; i[0]++) {
            wrapper.or(wq -> wq.eq(nonRepeatFields[i[0]], nonRepeatFields[i[0]].apply(entity)));
        }
        List<T> result = list(wrapper);
        return result != null && result.size() > 0;
    }

    protected boolean existAnd(T entity, SFunction<T, Object>... nonRepeatFields) {
        if (nonRepeatFields.length == 0) return false;
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        for (SFunction<T, Object> field : nonRepeatFields) {
            wrapper.eq(field, field.apply(entity));
        }
        List<T> result = list(wrapper);
        return result != null && result.size() > 0;
    }

    @Override
    public int insertIfNotExist(T entity, SFunction<T, Object>... nonRepeatFields) {
        return insertIfNotExist(entity, true, nonRepeatFields);
    }


    @Override
    public int insertIfNotExist(T entity, boolean useOr, SFunction<T, Object>... nonRepeatFields) {
        boolean exist = useOr ? existOr(entity, nonRepeatFields) : existAnd(entity, nonRepeatFields);
        if (exist) return 0;
        return save(entity) ? 1 : -1;
    }

    @Resource
    protected SqlSessionFactory sqlSessionFactory;

    @Override
    public boolean exeUseSqlSession(Consumer<SqlSession> fun) {
        try {
            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.SIMPLE, false);
//            fun.accept();
            return true;
        } catch (Exception e) {
//            sqlSessionBatch()
            log.error(e.getMessage());
            return false;
        }
    }
}
