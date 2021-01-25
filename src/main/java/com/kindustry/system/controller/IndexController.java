package com.kindustry.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kindustry.framework.controller.BaseController;

/**
 * <p>
 * 首页
 * </p>
 * 
 * @author hubin
 * @Date 2016-04-13
 */
@Controller
public class IndexController extends BaseController {

  /**
   * 首页
   */
  @RequestMapping("/index")
  public String index(Model model) {
    return "/index";
  }

  /**
   * 主页
   */
  @RequestMapping("/home")
  public String home() {
    return "/home";
  }

  /**
   * SW 捐赠
   */
  @RequestMapping("/donate")
  public String donate() {
    return "/donate";
  }
}
