package com.kindustry.framework.xsmart;

public class XSmartDynamicField extends XSmartField {
  private static final long serialVersionUID = 1L;
  private String property;
  private String onEmpty;

  public String getProperty() {
    return this.property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getOnEmpty() {
    return this.onEmpty;
  }

  public void setOnEmpty(String onEmpty) {
    this.onEmpty = onEmpty;
  }

  public boolean onEmpty() {
    return ("true".equalsIgnoreCase(this.onEmpty)) || ("yes".equalsIgnoreCase(this.onEmpty));
  }

  public XSmartDynamicField() {
  }

  public XSmartDynamicField(String property, String onEmpty) {
    this.property = property;
    this.onEmpty = onEmpty;
  }

  public StringBuffer getRawText(Object vo) {
    return getText(vo, true);
  }

  public StringBuffer getText(Object vo) {
    return getText(vo, false);
  }

  private StringBuffer getText(Object vo, boolean isRaw) {
    StringBuffer sb = null;
    boolean isEmptyValue = isEmpty(getBeanProperty(vo, this.property));
    if ((onEmpty()) && (isEmptyValue)) {
      sb = isRaw ? getChildrenRawText(vo) : getChildrenText(vo);
    }
    if ((!onEmpty()) && (!isEmptyValue)) {
      sb = isRaw ? getChildrenRawText(vo) : getChildrenText(vo);
    }
    // if (!isEmptyValue) {
    // sb = isRaw ? getChildrenRawText(vo) : getChildrenText(vo);
    // }
    return sb;
  }
}