package com.meituan.lyrebird.client.api.bandwidth;

import com.meituan.lyrebird.client.api.BaseResponse;

public class SpeedLimit extends BaseResponse {
  private int bandwidth;

  public int getBandwidth() {
    return bandwidth;
  }

  public void setBandwidth(int bandwidth) {
    this.bandwidth = bandwidth;
  }
}
