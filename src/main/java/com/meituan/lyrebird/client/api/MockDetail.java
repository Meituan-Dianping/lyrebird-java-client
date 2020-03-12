package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MockDetail {
  private String id;
  private String name;
  private String responseData;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("response")
  public void setResponseData(Map response) {
    this.responseData = response.get("data").toString();
  }

  public String getResponseData() {
    return responseData;
  }
}
