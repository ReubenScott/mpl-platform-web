package com.kindustry.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kindustry.framework.controller.BaseController;

/**
 * <p>
 * 监控
 * </p>
 * 
 * @author hubin
 * @Date 2016-04-21
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController extends BaseController {

  /**
   * 实时监控
   */
  @RequestMapping("/realTime")
  public String realTime(Model model) {

    return "/monitor/realTime";
  }

}
