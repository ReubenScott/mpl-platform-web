package com.kindustry.cashier.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kindustry.cashier.dao.CashierMapper;
import com.kindustry.cashier.entity.Goods;
import com.kindustry.cashier.service.ICashierService;
import com.kindustry.cashier.vo.DeviceInfo;
import com.kindustry.framework.service.imp.BaseServiceImp;

//import com.kindustry.edw.report.service.IReportService;
//import com.kindustry.framework.service.imp.BasicServiceImp;

@Service("cashierService")
public class CashierServiceImp extends BaseServiceImp implements ICashierService {

  @Resource
  private CashierMapper cashierMapper;

  @Override
  public boolean regester(DeviceInfo deviceinfo) {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * 通过条形码查询商品
   */
  @Override
  // 启用缓存
  @Cacheable(value = "goodsCache", key = "#barcode", unless = "#result == null")
  public Goods findGoodsByBarcode(String barcode) {
    // ApplicationListener.getBean("sqlSessionTemplate");
    System.out.println(cashierMapper);
    System.out.println("barcode : " + barcode);

    Goods good = cashierMapper.getGoodsByBarcode(barcode);
    // logger.debug("ReportServiceImp" , basicDao==null);
    return good; // this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }

  // 预支付接口 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额
  @Override
  public void prePayment(List<Goods> goods) {
    // ApplicationListener.getBean("sqlSessionTemplate");
    System.out.println(cashierMapper);

    // Goods element = super.getCacheBean("contentCache", barcode);
    // if(element!= null ){
    // System.out.println(JsonUtil.toJSONString(element));
    // }

    List keys = super.getCacheKeys("token");
    System.out.println(keys.size());
    for (Object obj : keys) {
      System.out.println(obj);
    }
    // logger.debug("ReportServiceImp" , basicDao==null);
  }

  // 支付接口 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额
  public boolean payment() {
    // 方式一: 现金支付 Cash payment

    // Goods element = super.getCacheBean("contentCache", barcode);
    // if(element!= null ){
    // System.out.println(JsonUtil.toJSONString(element));
    // }
    List keys = super.getCacheKeys("token");
    System.out.println(keys.size());
    for (Object obj : keys) {
      System.out.println(obj);
    }

    return true;
  }

}
