package com.kindustry.system.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindustry.common.util.JsonUtil;
import com.kindustry.framework.controller.BaseController;
import com.kindustry.support.dto.BaseDto;

/**
 * <p>
 * 邮件发送接收
 * </p>
 * 
 * @author hubin
 * @Date 2016-04-13
 */
@Controller
@RequestMapping("/sys/mail/")
public class MailController extends BaseController {

  /**
   * 发送
   */
  @RequestMapping("/send")
  public String send(Model model, String email) {
    // if (isPost()) {
    // model.addAttribute("email", email);
    // model.addAttribute("loginName", getSSOToken().getData());
    // boolean rlt = mailHelper.sendMail(email, "SpringWind 测试邮件！",
    // "/mail/tplSend.html", model);
    // String tipMsg = "发送邮件至【" + email + "】失败！！";
    // if(rlt){
    // tipMsg = "已成功发送邮件至【" + email + "】注意查收！！";
    // }
    // model.addAttribute("tipMsg", tipMsg);
    // }
    return "/mail/send";
  }

  @ResponseBody
  @RequestMapping(value = { "modifyGet" }, produces = { "application/json" })
  public Object addEmpGet() {
    // JsonUtil.toJSONString(obj)
    // JSONObject responseObj = new JSONObject();
    BaseDto rio = new BaseDto();

    rio.setSuccess(true);
    rio.setMsg("welcome," + "sssss ");

    rio.setData(new Date().getTime());

    return JsonUtil.toJSONString(rio);
    // return JsonUtil.toJSONString("id", 123/*reqObj.getIntValue("id")*/);
  }

}
