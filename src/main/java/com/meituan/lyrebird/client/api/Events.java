package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Events extends BaseResponse {
    private EventDetail[] events;
    private int page;
    @JsonProperty("page_count")
    private int pageCount;
    @JsonProperty("page_size")
    private int pageSize;

    public EventDetail[] getEvents() {
        return events;
    }

    public void setEvents(EventDetail[] events) {
        this.events = events;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }   
}