package com.kindustry.infoInquiry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kindustry.infoInquiry.service.SelfService;

public class Test {

  /**
   *　@param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    ApplicationContext app = new ClassPathXmlApplicationContext("modules/spring-aop.xml");
    SelfService busi = (SelfService)app.getBean("busiAop"); //这里写的必须是代理类
    busi.doBusi();
    busi.doBusiTo();
  }

}

