package com.yang.gotTickets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * swagger配置
 * bootstrap ui 页面
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {

            return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.yang.gotTickets.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger-api文档")
                .description("")
                //服务条款网址
                .termsOfServiceUrl("http://www.baidu.com")
                .version("1.0")
                .contact(new Contact("", "http://www.baidu.com", ""))
                .build();
    }

    private List<ApiKey> security() {
        List<ApiKey> apiKeys = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Auth", "Auth", "1");
        // ApiKey apiKey1 = new ApiKey("refresh_token", "refresh_token", "header");
        apiKeys.add(apiKey);
        //apiKeys.add(apiKey1);
        return apiKeys;
    }
}
