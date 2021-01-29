package com.kindustry.cashier.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindustry.cashier.entity.Goods;
import com.kindustry.cashier.service.ICashierService;
import com.kindustry.cashier.service.IPaymentService;
import com.kindustry.cashier.vo.DeviceInfo;
import com.kindustry.common.cache.EhcacheHelper;
import com.kindustry.common.util.BrowserUtil;
import com.kindustry.common.util.JsonUtil;
import com.kindustry.framework.annotation.Token;
import com.kindustry.framework.controller.BaseController;
import com.kindustry.support.dto.BaseDto;

@Controller
@RequestMapping("/cashier")
// 指定唯一一个*.do请求关联到该Controller @RequestParam(value = "username") String username
public class CashierController extends BaseController {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Resource
  private ICashierService cashierService;

  @Resource
  private IPaymentService paymentService;

  /**
   * 設備註冊
   * 
   * @param deviceinfo
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "regester", method = {RequestMethod.POST, RequestMethod.GET})
  public BaseDto regester(DeviceInfo deviceinfo) {
    cashierService.regester(deviceinfo);
    System.out.println(deviceinfo);

    BaseDto rio = new BaseDto();
    // rio.setSuccess(true);
    rio.setMsg("welcome");
    rio.setData(deviceinfo);

    return rio;
  }

  @ResponseBody
  @RequestMapping(value = "findGoods", method = {RequestMethod.POST, RequestMethod.GET})
  // , headers="Accept=application/json;charset=UTF-8"
    public
    BaseDto findGoods(@RequestParam(value = "barcode") String barcode) {
    // 如果不加任何参数，则在请求/test2/login.do时，便默认执行该方法 findGoodsByBarcode
    String agent = request.getHeader("user-agent");
    System.out.println(agent);
    System.out.println(BrowserUtil.getDeviceInfo1());
    System.out.println(cashierService);
    // String token = (String) getAttribute("token");
    Goods goods = cashierService.findGoodsByBarcode(barcode);
    goods.setStorageTime(new Date());
    // goods = paymentService.getBean(barcode);
    // System.out.println(goods.getName());
    // System.out.println(goods.getBarcode());
    // String ip = super.getParameter("ip");
    // String host = super.getParameter("host");
    // String mac = super.getParameter("mac");

    System.out.println(super.request);
    System.out.println(super.response);

    BaseDto rio = new BaseDto();
    // rio.setSuccess(true);
    rio.setMsg("welcome");
    rio.setData(goods);

    Goods element = EhcacheHelper.get("contentCache", barcode);
    // Goods element = cashierService.getCacheBean("contentCache", barcode);
    if (element != null) {
      System.out.println(JsonUtil.toJSONString(element));
    }

    System.out.println(JsonUtil.toJSONString(rio));
    // String content = JsonUtil.toJSONString(goods);
    // goods = JsonUtil.parse(content, Goods.class);
    // System.out.println(goods);
    // return JsonUtil.toJSONString(rio);
    return rio;
  }

  /**
   * 商品预支付 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额 服务端返回参数： Token(避免重复提交) 活动减免金额 ， 合计应付金额 （是否返回订单号讨论,
   * 商品明细服务端不需要缓存，客户端有可能会返回主页面修改 ） 缓存 Token
   * 
   * @param password
   * @return
   */
  @Token(save = true)
  @ResponseBody
  @RequestMapping(value = "prePayment", method = {RequestMethod.POST, RequestMethod.GET})
  public BaseDto prePayment(String username, String password) {
    String token = null; // super.getAttribute("token");

    String clientip = getClientIpAddress();
    logger.debug(clientip);
    logger.debug(token);

    // 生成客户端Token
    cashierService.putCacheBean("token", clientip, token);

    BaseDto rio = new BaseDto();
    rio.setSuccess(true);
    rio.setMsg("welcome," + token);
    rio.setData(token);

    List keys = cashierService.getCacheKeys("token");
    System.out.println(keys.size());
    for (Object obj : keys) {
      System.out.println(obj);
    }

    // cashierService.prePayment(barcode);

    return rio;
  }

  /**
   * 支付接口
   * 
   * @param username
   * @param password
   * @param age
   * @return
   */
  // @Token(save = true)
  @ResponseBody
  @RequestMapping(value = "payment", method = RequestMethod.POST)
  public BaseDto payment(String username, String password) {
    // 服务端缓存的 Token
    String clientip = getClientIpAddress();
    String stoken = cashierService.getCacheBean("token", clientip);
    logger.debug("token: " + stoken);

    // 客户端提交的参数
    // String ip = super.getParameter("ip");
    // String host = super.getParameter("host");
    // String mac = super.getParameter("mac");
    String ctoken = null; // super.getParameter("token");

    // System.out.println(ip);
    // System.out.println(host);
    // System.out.println(mac);
    System.out.println(ctoken);

    if (stoken != null && ctoken != null) {
      // 锁定缓存的各Token , 各个客户机不相互干预.
      synchronized (stoken) {
        // 防止重复提交。 重新刷新取一次缓存
        if (ctoken.equals(cashierService.getCacheBean("token", clientip))) {
          List<String> keys = cashierService.getCacheKeys("token");
          System.out.println(keys.size());
          for (String obj : keys) {
            System.out.println(obj);
          }

          // 支付成功清除缓存
          if (true) {
            cashierService.cacheEvict("token", clientip);
            logger.debug("TEST: " + clientip);
          }
        }
      }
    } else {
      // 服务端 没有缓存 Token 或 客户端没提交 Token
      logger.error("服务端 没有缓存  Token 或   客户端没提交 Token");
    }

    BaseDto rio = new BaseDto();
    rio.setSuccess(true);
    // rio.setMsg("welcome," + token);
    // rio.setData(token);

    return rio;
  }

}