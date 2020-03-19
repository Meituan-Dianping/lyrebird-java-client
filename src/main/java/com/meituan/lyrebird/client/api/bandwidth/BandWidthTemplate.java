package com.meituan.lyrebird.client.api.bandwidth;

public class BandWidthTemplate {
  private String templateName;

  public BandWidthTemplate(BandWidth bandWidth) {
    switch (bandWidth) {
      case MINIMUM:
        templateName = "2G";
        break;
      case LOW:
        templateName = "2.5G";
        break;
      case MEDIUM:
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
