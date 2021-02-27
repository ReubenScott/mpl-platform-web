package com.kindustry.orm.security;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.kindustry.common.security.CryptoHandler;

public class EncryptPropertyConfigurer extends PropertyPlaceholderConfigurer {

  /****************************** JDBC相关BEGIN ***************************************/
  public static final String JDBC_DRIVER = "jdbc.driverClassName";

  public static final String JDBC_URL = "jdbc.url";

  public static final String JDBC_USERNAME = "jdbc.username";

  public static final String JDBC_PASSWORD = "jdbc.password";

  /****************************** JDBC相关END ***************************************/

  protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
    String username = props.getProperty(JDBC_USERNAME);
    if (username != null) {
      props.setProperty(JDBC_USERNAME, CryptoHandler.decrypt(username));
    }

    String password = props.getProperty(JDBC_PASSWORD);
    if (password != null) {
      props.setProperty(JDBC_PASSWORD, CryptoHandler.decrypt(password));
    }

    String url = props.getProperty(JDBC_URL);
    if (url != null) {
      props.setProperty(JDBC_URL, CryptoHandler.decrypt(url));
    }

    String driverClassName = props.getProperty(JDBC_DRIVER);
    if (driverClassName != null) {
      props.setProperty(JDBC_DRIVER, CryptoHandler.decrypt(driverClassName));
    }

    super.processProperties(beanFactory, props);
  }
}