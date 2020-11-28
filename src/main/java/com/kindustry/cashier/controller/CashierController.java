package com.kindustry.cashier.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindustry.cashier.entity.Goods;
import com.kindustry.cashier.service.ICashierService;

@Controller
@RequestMapping("/cashier")
// 指定唯一一个*.do请求关联到该Controller @RequestParam(value = "username") String username
public class CashierController {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ICashierService cashierService;

  public ICashierService getCashierService() {
    return cashierService;
  }

  public void setCashierService(ICashierService cashierService) {
    this.cashierService = cashierService;
  }

  @ResponseBody
  @RequestMapping(value = "login.do", method = RequestMethod.GET)
  public String testLogin(@RequestParam(value = "barcode") String barcode) {
    // 如果不加任何参数，则在请求/test2/login.do时，便默认执行该方法

    System.out.println(cashierService);
    Goods goods = cashierService.findGoodsByBarcode(barcode, "");

    System.out.println(goods.getTitle());
    System.out.println(goods.getBarcode());

    if (!"admin".equals(barcode)) {
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