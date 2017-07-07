/*package com.soak.framework.xsmart;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

public class XSmartFactoryBean implements FactoryBean, InitializingBean {
  private static final long serialVersionUID = 1L;
  private static final Log logger = LogFactory.getLog(XSmartFactoryBean.class);
  public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
  protected DataSource dataSource;
  private Properties mappingLocations;
  private ResourceLoader resourceLoader;
  private XSmart instance;

  public DataSource getDataSource() {
    return this.dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Properties getMappingLocations() {
    return this.mappingLocations;
  }

  public void setMappingLocations(Properties mappingLocations) {
    this.mappingLocations = mappingLocations;
  }

  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public ResourceLoader getResourceLoader() {
    return this.resourceLoader;
  }

  public Object getObject() throws Exception {
    return this.instance;
  }

  public Class<?> getObjectType() {
    return XSmart.class;
  }

  public boolean isSingleton() {
    return true;
  }

  public void afterPropertiesSet() throws Exception {
    init();
  }

  protected void init() throws Exception {
    Connection conn = null;
    try {
      conn = this.dataSource.getConnection();
      DatabaseMetaData meta = conn.getMetaData();
      String productName = meta.getDatabaseProductName();
      String productVersion = meta.getDatabaseProductVersion();
      if (logger.isInfoEnabled()) {
        logger.info("Current Database is " + productName + ", Version is " + productVersion);
      }

      String location = getMappingLocation(productName, productVersion);
      if (logger.isInfoEnabled()) {
        logger.info("Loading database statement files from:[" + location + "]");
      }

      this.instance = new XSmart();
      this.instance.setResourceLoader(getResourceLoader());
      this.instance.setStatementLocations(StringUtils.tokenizeToStringArray(location, ",; \t\n"));
      this.instance.init();
    } catch (Exception e) {
      String s = "Cannot determine the database brand name";
      logger.error(s, e);
      throw new XSmartException(s, e);
    } finally {
      if (conn != null)
        conn.close();
    }
  }

  @SuppressWarnings("unchecked")
  protected String getMappingLocation(String productName, String productVersion) {
    if (this.mappingLocations.containsKey(productName)) {
      return this.mappingLocations.getProperty(productName);
    }

    PathMatcher matcher = new AntPathMatcher();
    Iterator it = this.mappingLocations.keySet().iterator();
    while (it.hasNext()) {
      String database = (String) it.next();
      if ((matcher.isPattern(database)) && (matcher.match(database, productName))) {
        return this.mappingLocations.getProperty(database);
      }
    }
    return "";
  }
}*/