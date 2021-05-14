package com.yang.gotTickets.config;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.yang.gotTickets.annotation.NoToken;
import com.yang.gotTickets.annotation.Authority;
import com.yang.gotTickets.bean.TResource;
import com.yang.gotTickets.bean.TRole;
import com.yang.gotTickets.exception.AuthenticationHandler;
import com.yang.gotTickets.filter.JwtTokenFilter;
import com.yang.gotTickets.service.TResourceService;
import com.yang.gotTickets.service.TRoleService;
import com.yang.gotTickets.util.AuthUtil;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @auther YF
 * @create 2021-04-24-21:40
 */
@Slf4j
@Configuration
//@Setter(onMethod_ = {@Autowired})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("张三").password("123").authorities(RoleProperties.USER).build());
//        manager.createUser(User.withUsername("李四").password("456").authorities(RoleProperties.ADMIN).build());
//        return manager;
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    private JwtTokenFilter tokenFilter;

    private RequestMappingHandlerMapping mappingHandlerMapping;

    private ProjectProperties properties;

    private AuthenticationHandler authenticationHandler;

    private TResourceService resourceService;

    private TRoleService roleService;

    private AuthUtil authUtil;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var handlerMethods = mappingHandlerMapping.getHandlerMethods();
        var resources = getUrlInfo(handlerMethods);
        var registry = http.authorizeRequests();
        // 过滤swagger等相关静态资源
        registry.antMatchers(properties.getHttpPermitArgs()).permitAll();
        // 允许添加 AccessNoToken注解的接口访问
        resources.forEach((k,v) -> {
            var matchers = registry.antMatchers(v, k.getResourceUrl());
            if (k.getAuthCode() == null) matchers.permitAll();
            else matchers.hasAuthority(k.getAuthCode());
        });
        registry.anyRequest()
                .authenticated()
                .and()
                //关闭CSRF保护
                .csrf().disable()
//                不使用session的方式认证
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

    private Map<TResource, HttpMethod> getUrlInfo(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {

        List<TRole> roleList = roleService.list();
        HashMap<Long, BigInteger> roleValue = new HashMap<>();
        for (TRole role : roleList) {
            roleValue.put(role.getId(), BigInteger.ZERO);
        }

        Map<TResource, HttpMethod> urlInfos = new HashMap<>();
        //遍历接口信息
        for (RequestMappingInfo info : handlerMethods.keySet()) {
            HandlerMethod handlerMethod = handlerMethods.get(info);
            //获取controller类的Authority注解
            Authority classAuthority = AnnotationUtil.getAnnotation(handlerMethod.getBeanType(), Authority.class);
            //获取接口对应方法的Authority注解
            Authority methodAuthority = handlerMethod.getMethodAnnotation(Authority.class);
            //获取接口对应方法的ApiOperation注解
            ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
            //获取接口对应方法的Authority注解
            NoToken classAccess = AnnotationUtil.getAnnotation(handlerMethod.getBeanType(), NoToken.class);
            //获取接口对应方法的NoToken注解
            NoToken methodAccess = handlerMethod.getMethodAnnotation(NoToken.class);
            PatternsRequestCondition patternsCondition = info.getPatternsCondition();
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            Set<String> patterns = new HashSet<>();
            if (patternsCondition != null) {
                patterns = patternsCondition.getPatterns();
            }
            boolean isAccess = classAccess != null || methodAccess != null;
            boolean isAuth = classAuthority != null || methodAuthority != null;
            ArrayList<Integer> roleIds = new ArrayList<>();
            if (classAuthority != null) {
                int[] value = classAuthority.value();
//                Collections.addAll(roleIds, value);
//                roleIds.addAll(ListUtil.toList())
            }
            for (RequestMethod method : methods) {
                for (String pattern : patterns) {
                    if (isAccess || isAuth) {
                        TResource build = TResource.builder()
                                .resourceUrl(pattern)
                                .description(apiOperation != null ? apiOperation.value() : null)
                                .authCode(isAuth && !isAccess ? authUtil.getNextCode() : null)
                                .build();
                        log.info(build.toString());
                        urlInfos.put(build, getHttpMethod(method));
                    }
                }
            }
        }
        return urlInfos;
    }

    private HttpMethod getHttpMethod(RequestMethod requestMethod) {
        return HttpMethod.resolve(requestMethod.name());
    }

}
