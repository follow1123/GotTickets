package com.yang.gotTickets.config.propertiesBean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther YF
 * @create 2021-05-28-14:41
 */
@Data
@Component
@ConfigurationProperties(prefix = "string-messages")
public class TestData {
    List<String> errMsgs;
}
