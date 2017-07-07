package com.kindustry.framework.xsmart;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XSmartTest {
  
  public static void main(String[] args) throws Exception {
    test1();
//    test2();
  }

  @SuppressWarnings("unchecked")
  public static void test1() throws Exception {
    String dir = System.getProperty("user.dir");
    XSmart xsmart = new XSmart();
    xsmart.setStatementResources(new File[] { new File(dir + "/src/main/java/com/soak/framework/xsmart/exampleSql.xml") });
    
    xsmart.init();

    Map params = new HashMap();
    String text = null;
    params.put("pro1", "vvv1");
    params.put("pro2", "vvv2");
    text = xsmart.getText("testSql", params);
    System.out.println("SQL=" + text);
    
    Map obj = xsmart.getParamMap("testSql", params);
    System.out.println("Param=" + obj);

    params.put("root", "true1");
    params.put("roleId", "roleId");
    text = xsmart.getText("findUncheckedMenuByRole", params);
    System.out.println("SQL=" + text);

    params.put("columns", "ID,NAME");
    params.put("table", "ODS.T_Amd_Test_123");
    params.put("conditions", "");
    params.put("orderBys", " order by ID");
    text = xsmart.getText("amend.queryRecord", params);
    System.out.println("SQL=" + text);
    xsmart.destroy();
  }

  @SuppressWarnings("unchecked")
  public static void test2() throws Exception {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "com/hongzhitech/xsmart/spring-xsmart.xml" });

    XSmart xsmart = (XSmart) context.getBean("xsmart");

    Map params = new HashMap();
    String text = null;
    params.put("pro1", "vvv1");
    params.put("pro2", "vvv2");
    text = xsmart.getText("testSql", params);
    System.out.println("SQL=" + text);
    Map obj = xsmart.getParamMap("testSql", params);
    System.out.println("Param=" + obj);

    params.put("root", "true");
    params.put("roleId", "roleId");
    text = xsmart.getText("findUncheckedMenuByRole", params);
    System.out.println("SQL=" + text);

    params.put("columns", "ID,NAME");
    params.put("table", "ODS.T_Amd_Test_123");
    params.put("conditions", "");
    params.put("orderBys", " order by ID");
    text = xsmart.getText("amend.queryRecord", params);
    System.out.println("SQL=" + text);
    context.close();
  }
}