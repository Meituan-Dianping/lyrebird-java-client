package com.meituan.lyrebird.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.JsonPath;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LBMockData extends BaseResponse {
  @JsonProperty("data")
  private Map data;

  public String getId() {
    return data.get("id").toString();
  }

  public String getName() {
    return data.get("name").toString();
  }

  public String getResponseData() {
    return JsonPath.parse(data.get("response")).read("$.data");
  }

  public Map getData() {
    return data;
  }

  public void setData(Map data) {
    this.data = data;
  }
}
