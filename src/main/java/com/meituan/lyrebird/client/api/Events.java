package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Events extends BaseResponse {
    private EventDetail[] eventDetails;
    private int page;
    @JsonProperty("page_count")
    private int pageCount;
    @JsonProperty("page_size")
    private int pageSize;

    public EventDetail[] getEvents() {
        return eventDetails;
    }

    public void setEvents(EventDetail[] eventDetails) {
        this.eventDetails = eventDetails;
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

    @Override
    public String toString() {
        return String.format("%n[page] %s,[page count] %s, [page size] %s%n%s%n", page, pageCount, pageSize, Arrays.toString(eventDetails));
    }
}