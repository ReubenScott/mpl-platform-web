package com.kindustry.framework.xsmart;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * ne不等于
 * eq相等
 * gt大于
 * ge大于等于
 * lt小于
 * le小于等于
 */
public class XSmartCompareField extends XSmartField
{
  private static final long serialVersionUID = 1L;
  private String compare;
  private String compareProperty;
  private String compareValue;
  public static final String AVAILABLE_COMPARE = "eq,ne,gt,ge,lt,le";

  public String getCompare()
  {
    return this.compare;
  }

  public void setCompare(String compare) {
    this.compare = compare;
  }

  public String getCompareProperty() {
    return this.compareProperty;
  }

  public void setCompareProperty(String compareProperty) {
    this.compareProperty = compareProperty;
  }

  public String getCompareValue() {
    return this.compareValue;
  }

  public void setCompareValue(String compareValue) {
    this.compareValue = compareValue;
  }

  public XSmartCompareField()
  {
  }

  public XSmartCompareField(String compare, String compareProperty, String compareValue) {
    this.compare = compare;
    this.compareProperty = compareProperty;
    this.compareValue = compareValue;
  }

  private static int getCompareOperateCode(String compare)
  {
    String[] ac = "eq,ne,gt,ge,lt,le".split(",");
    for (int i = 0; i < ac.length; ++i) {
      if (ac[i].equalsIgnoreCase(compare)) {
        return i;
      }
    }
    return -1;
  }

  public static boolean isAvailableCompare(String compare)
  {
    return (getCompareOperateCode(compare) < 0);
  }

  private static boolean match(String compare, Object value, String compareValue)
    throws NumberFormatException
  {
    if (value == null) {
      return false;
    }

    int reslt = -1;
    if ((value instanceof String)) {
      reslt = ((String)value).compareTo(compareValue);
    } else if ((value instanceof Integer)) {
      reslt = ((Integer)value).compareTo(new Integer(compareValue));
    } else if ((value instanceof Long)) {
      reslt = ((Long)value).compareTo(new Long(compareValue));
    } else if ((value instanceof Float)) {
      reslt = ((Float)value).compareTo(new Float(compareValue));
    } else if ((value instanceof Double)) {
      reslt = ((Double)value).compareTo(new Double(compareValue));
    } else if ((value instanceof Short)) {
      reslt = ((Short)value).compareTo(new Short(compareValue));
    } else if ((value instanceof BigInteger)) {
      reslt = ((BigInteger)value).compareTo(new BigInteger(compareValue));
    } else if ((value instanceof BigDecimal)) {
      reslt = ((BigDecimal)value).compareTo(new BigDecimal(compareValue));
    } else if ((value instanceof Boolean)) {
      int intBool = ((Boolean)value).booleanValue() ? 1 : 0;
      int intCompareBool = Boolean.valueOf(compareValue).booleanValue() ? 1 : 0;
      reslt = intBool - intCompareBool;
    }
    int code = getCompareOperateCode(compare);
    if (reslt == 0) {
      if ((code == 0) || (code == 3) || (code == 5))
        return true;
    }
    else if (reslt > 0) {
      if ((code == 1) || (code == 2) || (code == 3))
        return true;
    }
    else if ((reslt < 0) && (
      (code == 1) || (code == 4) || (code == 5))) {
      return true;
    }

    return false;
  }

  public StringBuffer getRawText(Object vo)
  {
    return getText(vo, true);
  }

  public StringBuffer getText(Object vo)
  {
    return getText(vo, false);
  }

  private StringBuffer getText(Object vo, boolean isRaw)
  {
    StringBuffer sb = null;
    Object actualValue = getBeanProperty(vo, this.compareProperty);
    if (match(this.compare, actualValue, this.compareValue)) {
      sb = (isRaw) ? getChildrenRawText(vo) : getChildrenText(vo);
    }
    return sb;
  }
}