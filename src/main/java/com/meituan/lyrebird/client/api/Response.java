package com.meituan.lyrebird.client.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jayway.jsonpath.JsonPath;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private int code;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, ?> data = new HashMap<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * 直接获取服务端返回数据对象
     * 
     * @return 服务端返回数据映射的 Java 对象
     */
    public Map<String, ?> getData() {
        return data;
    }

    /**
     * 传入 jsonPath 查询服务端返回数据中的对应内容
     * 
     * @param <T>
     * @param jsonPath   refer: https://github.com/json-path/JsonPath/edit/master/README.md
     * @return           根据 jsonPath 查询得到的返回数据
     */
    public <T> T getData(String jsonPath) {
        return JsonPath.parse(data).read(jsonPath);
    }

    /**
     * 传入 jsonPath, type 限制查询服务端返回数据的内容类型
     * 
     * @param <T>
     * @param jsonPath   refer: https://github.com/json-path/JsonPath/edit/master/README.md
     * @param type       expected return type (will try to map)
     * @return           根据 jsonPath 查询得到的返回数据
     */
    public <T> T getData(String jsonPath, Class<T> type) {
        return JsonPath.parse(data).read(jsonPath, type);
    }

    public void setData(Map<String, ?> data) {
        this.data = data;
    }
}