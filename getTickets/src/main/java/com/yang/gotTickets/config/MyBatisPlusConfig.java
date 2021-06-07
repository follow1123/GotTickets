package com.yang.gotTickets.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.util.SecurityContextUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.time.LocalDateTime;

/**
 * @auther YF
 * @create 2021-04-21-9:27
 */
@Slf4j
@Configuration
@Setter(onMethod_ = {@Autowired})
@MapperScan("com.yang.gotTickets.mapper")
public class MyBatisPlusConfig implements MetaObjectHandler {

    private SecurityContextUtil contextUtil;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        // mp插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // mybatis 插件设置
        sqlSessionFactoryBean.setPlugins(interceptor);
        GlobalConfig globalConfig = new GlobalConfig();
        // 配置属性填充 ，因为使用注入的方式 这个MetaObjectHandler对象组装不到mybatis 的配置内，所以自动注入数显会失效
        // 采用手动配置mybatis配置
        globalConfig.setMetaObjectHandler(this);
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    private Long getUserId() {
        return contextUtil.getUser().map(TUser::getId).orElse(0L);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createUserId", getUserId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时写入最新的update_time字段
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateUserId", getUserId(), metaObject);
    }
}
