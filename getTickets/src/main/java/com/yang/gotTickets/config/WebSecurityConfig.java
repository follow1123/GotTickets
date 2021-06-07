package com.yang.gotTickets.config;

import cn.hutool.core.lang.Assert;
import com.yang.gotTickets.bean.table.TResource;
import com.yang.gotTickets.config.propertiesBean.ProjectProperties;
import com.yang.gotTickets.config.propertiesBean.TestData;
import com.yang.gotTickets.filter.JwtTokenFilter;
import com.yang.gotTickets.handler.AuthenticationHandler;
import com.yang.gotTickets.service.TEnumFieldService;
import com.yang.gotTickets.service.TResourceService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * @auther YF
 * @create 2021-04-24-21:40
 */
@Slf4j
@Configuration
@Setter(onMethod_ = {@Autowired})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtTokenFilter tokenFilter;

    private ProjectProperties properties;

    private TestData testData;

    private TResourceService resourceService;

    private TEnumFieldService enumFieldService;

    private AuthenticationHandler authenticationHandler;

    private RequestMappingHandlerMapping mappingHandlerMapping;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        var handlerMethods = mappingHandlerMapping.getHandlerMethods();
        List<TResource> resources = resourceService.initResource(handlerMethods);
//        var resources = getUrlInfo(handlerMethods);
        var registry = http.authorizeRequests();
        // 过滤swagger等相关静态资源
        registry.antMatchers(properties.getHttpPermitArgs()).permitAll();
        // 允许添加 NoToken注解的接口访问
        resources.forEach(r -> {
            var matchers = registry.antMatchers(getHttpMethod(r.getMethod()), r.getResourceUrl());
            if (r.getHaveAuth() != null) {
                if (r.getHaveAuth()) matchers.hasAuthority(r.getAuthCode());
                else matchers.permitAll();
            }
        });
        registry.anyRequest()
                .authenticated()
                .and()
                //关闭CSRF保护
                .csrf().disable()
                // 不使用session的方式认证
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationHandler)
                .accessDeniedHandler(authenticationHandler)
                .and()
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }


    private HttpMethod getHttpMethod(Byte code) {
        return HttpMethod.resolve(enumFieldService.getEnumValueMap("请求方式").get(code));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
