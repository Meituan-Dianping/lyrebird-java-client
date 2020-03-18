package com.meituan.lyrebird.client.api.bandwidth;

public class BandWidthTemplate {
  private String templateName;

  public BandWidthTemplate(String bandWidth) {
    templateName = bandWidth;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }
}
