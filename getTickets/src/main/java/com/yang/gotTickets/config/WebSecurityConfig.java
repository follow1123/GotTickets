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
        // ??????swagger?????????????????????
        registry.antMatchers(properties.getHttpPermitArgs()).permitAll();
        // ???????????? NoToken?????????????????????
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
                //??????CSRF??????
                .csrf().disable()
                // ?????????session???????????????
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
        return HttpMethod.resolve(enumFieldService.getEnumValueMap("????????????").get(code));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
