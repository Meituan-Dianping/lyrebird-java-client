package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mock {
  @JsonProperty("data")
  private MockDetail mockDetail;

  public MockDetail getMockDetail() {
    return mockDetail;
  }

  public void setMockDetail(MockDetail mockDetail) {
    this.mockDetail = mockDetail;
  }
}
