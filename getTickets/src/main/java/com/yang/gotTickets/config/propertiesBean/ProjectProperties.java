package com.yang.gotTickets.config.propertiesBean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @auther YF
 * @create 2021-04-28-15:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectProperties {

    //小时为单位
    private Integer tokenExpire = 7 * 24;

    private String[] httpPermitArgs;

    private Map<String, String> urls;

    public String getUrl(String key){
        return urls.get(key);
    }

}
