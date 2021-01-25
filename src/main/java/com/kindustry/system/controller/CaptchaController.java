package com.kindustry.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindustry.framework.web.BaseController;


/**
 * <p>
 * 验证码服务
 * </p>
 * 
 * @author hubin
 * @Date 2016-01-06
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController extends BaseController {

	/**
	 * 生成图片
	 */
	@ResponseBody
	@RequestMapping("/image")
	public void image(String ctoken) {
//		try {
//			MyCaptcha.getInstance().generate(request, response.getOutputStream(), ctoken);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
