package com.meituan.lyrebird.client.api.bandwidth;

public class BandwidthTemplate {
  private String templateName;

  public BandwidthTemplate(Bandwidth bandwidth) {
    switch (bandwidth) {
      case BANDWIDTH_2G:
        templateName = "2G";
        break;
      case BANDWIDTH_2_5G:
        templateName = "2.5G";
        break;
      case BANDWIDTH_3G:
        templateName = "3G";
        break;
      default:
        templateName = "UNLIMITED";
        break;
    }
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }
}
