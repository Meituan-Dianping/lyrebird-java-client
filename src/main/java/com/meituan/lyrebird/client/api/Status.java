package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Status extends BaseResponse{
    @JsonProperty("mock.port")
    private int mockPort;
    @JsonProperty("proxy.port")
    private int proxyPort;
    private String ip;

    public int getMockPort(){
        return mockPort;
    }

    public int getProxyPort(){
        return proxyPort;
    }

    public String getIp(){
        return ip;
    }
}