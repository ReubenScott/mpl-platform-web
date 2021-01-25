package com.kindustry.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindustry.framework.dto.BaseDto;
import com.kindustry.framework.controller.BaseController;

//import com.baomidou.framework.controller.SuperController;
//import com.baomidou.kisso.SSOConfig;
//import com.baomidou.kisso.SSOHelper;
//import com.baomidou.kisso.SSOToken;
//import com.baomidou.kisso.annotation.Action;
//import com.baomidou.kisso.annotation.Login;
//import com.baomidou.kisso.annotation.Permission;
//import com.baomidou.kisso.common.encrypt.SaltEncoder;
//import com.baomidou.kisso.common.util.RandomUtil;
//import com.baomidou.kisso.web.waf.request.WafRequestWrapper;
//import com.baomidou.springwind.common.MyCaptcha;
//import com.baomidou.springwind.common.enums.UserType;
//import com.baomidou.springwind.entity.User;
//import com.baomidou.springwind.service.IUserService;

/**
 * <p>
 * 账户相关操作
 * </p>
 * 
 * @author hubin
 * @Date 2016-04-13
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  // 锁定用户标记
  private static final String LOCKSCREEN_USER_FLAG = "LockscreenUserFlag";

  // @Autowired
  // protected IUserService userService;

  @ResponseBody
  @RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
  public BaseDto findGoods(@RequestParam(value = "username") String barcode) {
    // 如果不加任何参数，则在请求/test2/login.do时，便默认执行该方法 findGoodsByBarcode
    // goods = paymentService.getBean(barcode);
    // System.out.println(goods.getName());
    // System.out.println(goods.getBarcode());

    logger.debug(barcode);
    // String ip = super.getParameter("ip");
    // String host = super.getParameter("host");
    // String mac = super.getParameter("mac");
    //
    // System.out.println(ip);
    // System.out.println(host);
    // System.out.println(mac);

    BaseDto rio = new BaseDto();
    rio.setSuccess(true);

    // String content = JsonUtil.toJSONString(goods);
    // goods = JsonUtil.parse(content, Goods.class);
    // System.out.println(goods);
    // return JsonUtil.toJSONString(rio);
    return rio;
  }

  /**
   * 登录
   * 
   * @RequestMapping("/login") public String index(Model model) { if (isPost())
   *                           { String errorMsg = "用户名或密码错误"; // * 过滤 XSS SQL
   *                           注入 WafRequestWrapper wr = new
   *                           WafRequestWrapper(request); String ctoken =
   *                           wr.getParameter("ctoken"); String captcha =
   *                           wr.getParameter("captcha"); if
   *                           (StringUtils.isNotBlank(ctoken) &&
   *                           StringUtils.isNotBlank(captcha) &&
   *                           MyCaptcha.getInstance().verification(wr, ctoken,
   *                           captcha)) { String loginName =
   *                           wr.getParameter("loginName"); String password =
   *                           wr.getParameter("password"); // * 用户存在，签名合法，登录成功
   *                           * 进入后台 User user =
   *                           userService.selectByLoginName(loginName); if
   *                           (user != null &&
   *                           SaltEncoder.md5SaltValid(loginName,
   *                           user.getPassword(), password)) { SSOToken st =
   *                           new SSOToken(request); st.setId(user.getId());
   *                           st.setData(loginName);
   * 
   *                           // 记住密码，设置 cookie 时长 1 周 = 604800 秒 String
   *                           rememberMe = wr.getParameter("rememberMe"); if
   *                           ("on".equals(rememberMe)) {
   *                           request.setAttribute(SSOConfig.SSO_COOKIE_MAXAGE,
   *                           604800); }
   * 
   *                           SSOHelper.setSSOCookie(request, response, st,
   *                           true); return redirectTo("/index.html"); } } else
   *                           { errorMsg = "验证码错误"; }
   *                           model.addAttribute("errorMsg", errorMsg); }
   *                           model.addAttribute(MyCaptcha.CAPTCHA_TOKEN,
   *                           RandomUtil.get32UUID()); return "/login"; }
   */

  /**
   * 注册
   * 
   * @RequestMapping("/register") public String register(Model model, User user)
   *                              { if (isPost()) { User existUser =
   *                              userService.
   *                              selectByLoginName(user.getLoginName()); if
   *                              (existUser == null) { // 演示不验证表单，用户名作为密码盐值
   *                              user
   *                              .setPassword(SaltEncoder.md5SaltEncode(user
   *                              .getLoginName(), user.getPassword()));
   *                              user.setType(UserType.MEMBER.key());
   *                              user.setCrTime(new Date());
   *                              user.setLastTime(user.getCrTime()); boolean
   *                              rlt = userService.insert(user); if (rlt) {
   * 
   *                              //* 注册成功，自动登录进入后台
   * 
   *                              SSOToken st = new SSOToken(request);
   *                              st.setId(user.getId());
   *                              st.setData(user.getLoginName());
   *                              SSOHelper.setSSOCookie(request, response, st,
   *                              true); return redirectTo("/index.html"); } }
   *                              else { model.addAttribute("tipMsg", "注册用户名【" +
   *                              user.getLoginName() + "】已存在！"); } } return
   *                              "/register"; }
   */

  /**
   * 退出
   * 
   * @RequestMapping("/logout") public String logout(Model model) {
   *                            SSOHelper.clearLogin(request, response); return
   *                            redirectTo("/account/login.html"); }
   */

  /**
   * 锁定
   * 
   * @RequestMapping("/lockscreen") public String lockscreen(Model model, String
   *                                password) { HttpSession session =
   *                                request.getSession(); String loginName =
   *                                (String)
   *                                session.getAttribute(LOCKSCREEN_USER_FLAG);
   *                                if (StringUtils.isBlank(loginName)) {
   *                                SSOToken st = SSOHelper.getToken(request);
   *                                if (st == null) { //未登录 return
   *                                redirectTo("/account/login.html"); }
   *                                loginName = st.getData();
   *                                session.setAttribute(LOCKSCREEN_USER_FLAG,
   *                                loginName);; SSOHelper.clearLogin(request,
   *                                response); } else if
   *                                (StringUtils.isNotBlank(password) &&
   *                                isPost()) {
   * 
   *                                //* 锁定页面登录 User user =
   *                                userService.selectByLoginName(loginName); if
   *                                (user != null &&
   *                                SaltEncoder.md5SaltValid(loginName,
   *                                user.getPassword(), password)) {
   * 
   *                                //* 登录成功，进入后台
   * 
   *                                SSOToken st = new SSOToken(request);
   *                                st.setId(user.getId());
   *                                st.setData(loginName);
   *                                SSOHelper.setSSOCookie(request, response,
   *                                st, true); return redirectTo("/index.html");
   *                                } }
   * 
   *                                model.addAttribute("loginName", loginName);
   *                                return "/lockscreen"; }
   */

}
