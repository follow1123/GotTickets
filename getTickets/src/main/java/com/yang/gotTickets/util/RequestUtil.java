package com.yang.gotTickets.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @auther YF
 * @create 2021-05-15-14:49
 */
@Slf4j
@Component
public class RequestUtil {

    public <R, P> R request(String url, Method method, P param, Class<R> resultType) {
        if (StrUtil.isBlank(url)) {
            log.error("链接为空");
            return null;
        }
        log.info("准备链接：" + url);
        HttpRequest request = new HttpRequest(url).method(method);
        if (param != null) {

            Map<String, Object> bean;
            if (param instanceof Map){
                bean = (Map<String, Object>) param;
            }else {
                bean = BeanUtil.beanToMap(param);
            }
            log.info("准备参数：" + bean);
            request.form(bean);
        }
        HttpResponse response = null;
        try {
            response = request.execute();
            String body = response.body();
            log.info("获取结果：" + body);
            return JSONUtil.toBean(body, resultType);
        } catch (Exception e) {
            log.error("请求异常：" + e.getMessage());
        } finally {
            if (response != null)
                response.close();
        }
        return null;
    }

    public <R, P> R post(String url, P param, Class<R> resultType) {
        return request(url, Method.POST, param, resultType);
    }
    public <R, P> R get(String url, P param, Class<R> resultType) {
        return request(url, Method.GET, param, resultType);
    }

    public <P> JSONObject post(String url, P param) {
        return post(url, param, JSONObject.class);
    }
    public <P> JSONObject get(String url, P param) {
        return get(url, param, JSONObject.class);
    }

    public JSONObject post(String url) {
        return post(url, null);
    }
    public JSONObject get(String url) {
        return get(url, null);
    }

}
