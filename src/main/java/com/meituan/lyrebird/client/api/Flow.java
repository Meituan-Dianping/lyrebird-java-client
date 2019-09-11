package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flow {
    private String id;
    private Request request;
    private Response response;
    private Double duration;
    @JsonProperty("start_time")
    private Double startTime;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDuration() {
        return duration;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getStartTime() {
        return startTime;
    }
}
