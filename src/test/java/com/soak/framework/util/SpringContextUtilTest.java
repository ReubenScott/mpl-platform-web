package com.soak.framework.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.soak.framework.cache.EhCacheImpl;

public class SpringContextUtilTest {

  @Before
  public void setUp()
    throws Exception {
  }

  @Test
  public void testGetBean() {
    
    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
    EhCacheImpl aa =  SpringContextUtil.getBean("ehCache");
    System.out.println(ac);
    System.out.println(aa);
  }

}
