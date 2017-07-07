package com.kindustry.framework.xsmart.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

import com.kindustry.framework.xsmart.XSmartCompareField;
import com.kindustry.framework.xsmart.XSmartDynamicField;
import com.kindustry.framework.xsmart.XSmartException;
import com.kindustry.framework.xsmart.XSmartField;
import com.kindustry.framework.xsmart.XSmartTextField;

public class XSmartDom4jReader implements XSmartReader {
  @SuppressWarnings("unchecked")
  public Map<String, List<XSmartField>> read(InputStream in) throws IOException {
    Map result = new HashMap();
    SAXReader xmlReader = new SAXReader();
    xmlReader.setMergeAdjacentText(true);
    try {
      Document doc = xmlReader.read(in);
      Element root = doc.getRootElement();
      List statements = root.elements("statement");
      for (int i = 0; i < statements.size(); ++i) {
        Element stmt = (Element) statements.get(i);
        String id = stmt.attributeValue("id");
        if ((id == null) || ("".equals(id.trim()))) {
          throw new XSmartException("statement's \"id\" should not be empty!");
        }
        if (result.containsKey(id)) {
          throw new XSmartException("statement named \"" + id + "\" has bean defined!");
        }

        List fields = stmt.content();
        result.put(id, readFields(fields));
      }
    } catch (DocumentException e) {
      throw new XSmartException("statement file is error!", e);
    } catch (Exception e) {
      throw new XSmartException("statement file not exist!", e);
    } finally {
      if (in != null) {
        in.close();
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private List<XSmartField> readFields(List<?> fields) {
    List result = new ArrayList();
    for (int j = 0; j < fields.size(); ++j) {
      String text;
      if (fields.get(j) instanceof Text) {
        text = ((Text) fields.get(j)).getText().trim();
        if (!("".equals(text))) {
          result.add(new XSmartTextField(text));
        }
      } else if (fields.get(j) instanceof CDATA) {
        text = ((CDATA) fields.get(j)).getText().trim();
        if (!("".equals(text))) {
          result.add(new XSmartTextField(text));
        }
      } else if (fields.get(j) instanceof Element) {
        Element field = (Element) fields.get(j);
        if ("dynamic-field".equals(field.getName())) {
          result.add(readDynamicField(field));
        } else if ("compare-field".equals(field.getName())) {
          result.add(readCompareField(field));
        } else {
          throw new XSmartException("\"" + field.getName() + "\" is not supportable!");
        }
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private XSmartDynamicField readDynamicField(Element field) {
    String property = field.attributeValue("property");
    String onEmpty = field.attributeValue("onEmpty");
    if ((property == null) || ("".equals(property))) {
      throw new XSmartException("\"property\" of dynamic-field is required!");
    }
    XSmartDynamicField f = new XSmartDynamicField(property, onEmpty);
    List children = readFields(field.content());
    f.getChildren().addAll(children);
    return f;
  }

  @SuppressWarnings("unchecked")
  private XSmartCompareField readCompareField(Element field) {
    String compare = field.attributeValue("compare");
    String compareProperty = field.attributeValue("compareProperty");
    String compareValue = field.attributeValue("compareValue");
    if ((compare == null) || ("".equals(compare))) {
      throw new XSmartException("Attribute \"compare\" of compare-field is required!");
    }
    if ((compareProperty == null) || ("".equals(compareProperty))) {
      throw new XSmartException("Attribute \"compareProperty\" of compare-field is required!");
    }
    if ((compareValue == null) || ("".equals(compareValue))) {
      throw new XSmartException("Attribute \"compareValue\" of compare-field is required!");
    }
    if (!(XSmartCompareField.isAvailableCompare(compare))) {
      throw new XSmartException("Attribute \"compare\" of compare-field is unavailable, the available value is (eq,ne,gt,ge,lt,le)!");
    }

    XSmartCompareField f = new XSmartCompareField(compare, compareProperty, compareValue);
    List children = readFields(field.content());
    f.getChildren().addAll(children);
    return f;
  }
}