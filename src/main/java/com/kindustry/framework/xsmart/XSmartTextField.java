package com.kindustry.framework.xsmart;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;

public class XSmartTextField extends XSmartField
{
	private static final long serialVersionUID = 1L;
	  private String value = "";

	  public String getValue() {
	    return this.value;
	  }

	  public void setValue(String value) {
	    this.value = value;
	  }

	  public XSmartTextField()
	  {
	  }

	  public XSmartTextField(String value) {
	    this.value = value;
	  }

	  public StringBuffer getText(Object vo)
	  {
	    StringBuffer sb = getRawText(vo);
	    Matcher m = PLACEHOLDER.matcher(sb);
	    while (m.find(0)) {
	      sb.replace(m.start(), m.end(), "?");
	    }
	    return sb;
	  }

	  public StringBuffer getRawText(Object vo)
	  {
	    StringBuffer sb = new StringBuffer(this.value);
	    Matcher m = REPLACEMENT.matcher(sb);
	    while (m.find(0)) {
	      String placeholder = sb.substring(m.start(), m.end());
	      String property = placeholder.substring(1, placeholder.length() - 1);
	      StringBuffer replace = getReplacementText(getBeanProperty(vo, property));
	      sb.replace(m.start(), m.end(), replace.toString());
	    }
	    return sb;
	  }

	  private StringBuffer getReplacementText(Object value)
	  {
	    if (value == null) {
	      return new StringBuffer();
	    }
	    if ((value instanceof Object[])) {
	      return getReplacementTextFromArray((Object[])value);
	    }
	    if ((value instanceof Collection)) {
	      return getReplacementTextFromCollection((Collection)value);
	    }
	    return new StringBuffer().append(value);
	  }

	  private StringBuffer getReplacementTextFromArray(Object[] array)
	  {
	    if ((array == null) || (array.length == 0)) {
	      return new StringBuffer();
	    }
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < array.length; i++) {
	      if (i > 0) sb.append(",");
	      sb.append(array[i]);
	    }
	    return sb;
	  }

	  private StringBuffer getReplacementTextFromCollection(Collection<?> collection)
	  {
	    if ((collection == null) || (collection.isEmpty())) {
	      return new StringBuffer();
	    }
	    StringBuffer sb = new StringBuffer();
	    int i = 0;
	    for (Iterator it = collection.iterator(); it.hasNext(); i++) {
	      if (i > 0) sb.append(",");
	      sb.append(it.next());
	    }
	    return sb;
	  }
}