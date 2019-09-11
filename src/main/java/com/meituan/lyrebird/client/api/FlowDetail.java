package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowDetail extends BaseResponse {
    @JsonProperty("data")
    private Flow flow;

    public Request getRequest() throws LyrebirdClientException {
        if (super.getCode() != 1000) {
            throw new LyrebirdClientException("/api/flow/{flowId} 接口返回异常：" + super.getMessage());
        }
        return flow.getRequest();
    }

    public Response getResponse() throws LyrebirdClientException {
        if (super.getCode() != 1000) {
            throw new LyrebirdClientException("/api/flow/{flowId} 接口返回异常：" + super.getMessage());
        }
        return flow.getResponse();
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }
}