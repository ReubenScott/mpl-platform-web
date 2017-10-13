package com.kindustry.framework.xsmart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.beanutils.PropertyUtils;

@SuppressWarnings("unchecked")
public abstract class XSmartField
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public static final String SPACE = " ";
  public static final Pattern PLACEHOLDER = Pattern.compile("#[\\w]+#");
  public static final Pattern REPLACEMENT = Pattern.compile("\\$[\\w]+\\$");

  protected List<XSmartField> children = new ArrayList();

  public List<XSmartField> getChildren()
  {
    return this.children;
  }

  protected StringBuffer getChildrenRawText(Object vo)
  {
    StringBuffer sb = new StringBuffer();
    for (Iterator it = this.children.iterator(); it.hasNext(); ) {
      StringBuffer s = ((XSmartField)it.next()).getRawText(vo);
      if ((s != null) && (s.length() > 0)) {
        sb.append(s).append(" ");
      }
    }
    return sb;
  }

  protected StringBuffer getChildrenText(Object vo)
  {
    StringBuffer sb = new StringBuffer();
    for (Iterator it = this.children.iterator(); it.hasNext(); ) {
      StringBuffer s = ((XSmartField)it.next()).getText(vo);
      if ((s != null) && (s.length() > 0)) {
        sb.append(s).append(" ");
      }
    }
    return sb;
  }

  public abstract StringBuffer getRawText(Object paramObject);

  public abstract StringBuffer getText(Object paramObject);

  public List<Object> getParam(Object vo)
  {
    StringBuffer sb = getRawText(vo);
    if (sb == null) {
      return null;
    }
    List result = new ArrayList();
    Matcher m = PLACEHOLDER.matcher(sb);
    while (m.find()) {
      String placeholder = sb.substring(m.start(), m.end());
      String property = placeholder.substring(1, placeholder.length() - 1);
      result.add(getBeanProperty(vo, property));
    }
    return result;
  }

  public Map<String, Object> getParamMap(Object vo)
  {
    StringBuffer sb = getRawText(vo);
    if (sb == null) {
      return null;
    }
    Map result = new HashMap();
    Matcher m = PLACEHOLDER.matcher(sb);
    while (m.find()) {
      String placeholder = sb.substring(m.start(), m.end());
      String property = placeholder.substring(1, placeholder.length() - 1);
      result.put(property, getBeanProperty(vo, property));
    }
    return result;
  }

  protected Object getBeanProperty(Object vo, String property)
  {
//    if (vo == null)
      return null;
//    try
//    {
//      return PropertyUtils.getProperty(vo, property);
//    } catch (NoSuchMethodException e) {
//      return null;
//    } catch (Exception e) {
//      throw new XSmartException("property \"" + property + "\" is missing!", e);
//    }
  }

  public static final boolean isEmpty(Object value)
  {
    if (value == null) {
      return true;
    }
    if (value instanceof String) {
      return "".equals(((String)value).trim());
    }
    return false;
  }
}