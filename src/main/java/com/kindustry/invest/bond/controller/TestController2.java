package com.kindustry.invest.bond.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test2/login.do")
// 指定唯一一个*.do请求关联到该Controller
public class TestController2 {

  @RequestMapping
  public String testLogin(String username, String password, int age) {
    // 如果不加任何参数，则在请求/test2/login.do时，便默认执行该方法

    if (!"admin".equals(username) || !"admin".equals(password) || age < 5) {
      return "loginError";
    }
    return "loginSuccess";
  }

  @RequestMapping(params = "method=1", method = RequestMethod.POST)
  public String testLogin2(String username, String password) {
    // 依据params的参数method的值来区分不同的调用方法
    // 可以指定页面请求方式的类型，默认为get请求

    if (!"admin".equals(username) || !"admin".equals(password)) {
      return "loginError";
    }
    return "loginSuccess";
  }

  @RequestMapping(params = "method=2")
  public String testLogin3(String username, String password, int age) {
    if (!"admin".equals(username) || !"admin".equals(password) || age < 5) {
      return "loginError";
    }
    return "loginSuccess";
  }
}