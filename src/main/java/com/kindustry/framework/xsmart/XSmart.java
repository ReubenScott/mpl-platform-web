package com.kindustry.framework.xsmart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.kindustry.framework.xsmart.reader.XSmartDom4jReader;
import com.kindustry.framework.xsmart.reader.XSmartReader;

public class XSmart implements Serializable {
  private static final long serialVersionUID = 1L;
  private XSmart parent;
  private Map<String, List<XSmartField>> statements;
  private boolean ready = false;
  private String[] statementLocations;
  private Resource[] statementRecorces;
  private boolean merge = false;
  private XSmartReader reader;
  private ResourceLoader resourceLoader;

  @SuppressWarnings("unchecked")
  public XSmart() {
    this.statements = new HashMap();
  }

  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public ResourceLoader getResourceLoader() {
    if (this.resourceLoader == null) {
      this.resourceLoader = new PathMatchingResourcePatternResolver();
    }
    return this.resourceLoader;
  }

  public void setStatementResources(File[] statementRecorces) {
    this.statementRecorces = new Resource[statementRecorces.length];
    for (int i = 0; i < statementRecorces.length; ++i)
      this.statementRecorces[i] = new FileSystemResource(statementRecorces[i]);
  }

  public void setStatementResources(Resource[] statementRecorces) {
    this.statementRecorces = statementRecorces;
  }

  public String[] getStatementLocations() {
    return this.statementLocations;
  }

  public void setStatementLocations(String[] statementLocations) {
    this.statementLocations = statementLocations;
  }

  public Resource[] getStatementReources() {
    return this.statementRecorces;
  }

  public boolean isReady() {
    return this.ready;
  }

  public XSmart getParent() {
    return this.parent;
  }

  public void setParent(XSmart parent) {
    this.parent = parent;
  }

  public boolean isMerge() {
    return this.merge;
  }

  public void setMerge(boolean merge) {
    this.merge = merge;
  }

  public void setReader(XSmartReader reader) {
    this.reader = reader;
  }

  public XSmartReader getReader() {
    if (this.reader == null) {
      this.reader = new XSmartDom4jReader();
    }
    return this.reader;
  }

  @SuppressWarnings("unchecked")
  protected Set<Resource> getActualResources() throws IOException {
    Set actualResources = new HashSet();

    if (this.statementRecorces != null) {
      actualResources.addAll(Arrays.asList(this.statementRecorces));
    }

    if (this.statementLocations != null) {
      ResourceLoader resourceLoader = getResourceLoader();
      if (resourceLoader == null) {
        throw new XSmartException("Cannot load statement files: no ResourceLoader available");
      }
      for (String location : this.statementLocations) {
        if (resourceLoader instanceof ResourcePatternResolver) {
          Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
          actualResources.addAll(Arrays.asList(resources));
        } else {
          Resource resource = resourceLoader.getResource(location);
          actualResources.add(resource);
        }
      }
    }

    return actualResources;
  }

  @SuppressWarnings("unchecked")
  public void init() throws IOException {
    this.ready = false;
    this.statements.clear();

    Set actualResources = getActualResources();
    for (Iterator it = actualResources.iterator(); it.hasNext();) {
      Resource resource = (Resource) it.next();
      config(resource.getInputStream());
    }
    if ((isMerge()) && (this.parent != null)) {
      mergeTo(this.statements, this.parent);
    }
    this.ready = true;
  }

  @SuppressWarnings("unchecked")
  private void mergeTo(Map<String, List<XSmartField>> map, XSmart target) {
    for (Iterator it = map.keySet().iterator(); it.hasNext();) {
      String key = (String) it.next();
      if (target.statements.containsKey(key)) {
        throw new XSmartException("statement of id=\"" + key + "\" has bean defined!");
      }
      target.statements.put(key, (List) map.get(key));
    }
  }

  @SuppressWarnings("unchecked")
  public void config(InputStream in) throws IOException {
    XSmartReader reader = getReader();
    Map map = reader.read(in);
    mergeTo(map, this);
  }

  public void destroy() {
    this.statements.clear();
    this.ready = false;
  }

  public void refresh() throws IOException {
    destroy();
    init();
  }

  @SuppressWarnings("unchecked")
  public List<XSmartField> getStatement(String id) {
    if (!(this.statements.containsKey(id))) {
      if (this.parent == null) {
        throw new XSmartException("statement [id=\"" + id + "\"]: not existed");
      }
      return this.parent.getStatement(id);
    }
    return ((List) this.statements.get(id));
  }

  public String getText(String id) {
    return getText(id, null);
  }

  @SuppressWarnings("unchecked")
  public String getText(String id, Object vo) {
    List fileds = getStatement(id);
    StringBuffer sb = new StringBuffer();
    try {
      for (int i = 0; i < fileds.size(); ++i) {
        StringBuffer s = ((XSmartField) fileds.get(i)).getText(vo);
        if ((s != null) && (s.length() > 0))
          sb.append(s).append(" ");
      }
    } catch (NumberFormatException e) {
      throw new XSmartException("statement [id=\"" + id + "\"]: cannot format number " + e.getMessage());
    } catch (Exception e) {
      throw new XSmartException("statement [id=\"" + id + "\"]: " + e.getMessage(), e);
    }
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  public Object[] getParam(String id, Object vo) {
    List fileds = getStatement(id);
    List result = new ArrayList();
    for (int i = 0; i < fileds.size(); ++i) {
      List list = ((XSmartField) fileds.get(i)).getParam(vo);
      if ((list != null) && (!(list.isEmpty()))) {
        result.addAll(list);
      }
    }
    return result.toArray();
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> getParamMap(String id, Object vo) {
    if (!(this.statements.containsKey(id))) {
      throw new XSmartException("statement [id=\"" + id + "\"]: not existed");
    }
    Map result = new HashMap();
    List fileds = getStatement(id);
    for (int i = 0; i < fileds.size(); ++i) {
      Map map = ((XSmartField) fileds.get(i)).getParamMap(vo);
      if ((map != null) && (!(map.isEmpty()))) {
        result.putAll(map);
      }
    }
    return result;
  }
}