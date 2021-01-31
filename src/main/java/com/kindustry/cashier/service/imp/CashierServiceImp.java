package com.kindustry.cashier.service.imp;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kindustry.cashier.dao.bridge.CashierMapperBridge;
import com.kindustry.cashier.entity.GoodsEntity;
import com.kindustry.cashier.service.ICashierService;
import com.kindustry.cashier.vo.DeviceInfo;
import com.kindustry.framework.service.imp.BaseServiceImp;

@Service
public class CashierServiceImp extends BaseServiceImp implements ICashierService {

  @Autowired
  private CashierMapperBridge cashierMapper;

  @Override
  public boolean regester(DeviceInfo deviceinfo) {
    // TODO Auto-generated method stub
    GoodsEntity good = new GoodsEntity();
    good.setBarcode("6927241400304");
    good.setTitle("华萍针线盒");

    GoodsEntity good1 = new GoodsEntity();
    good1.setBarcode("6916834061886");
    good1.setTitle("华萍针线盒");

    GoodsEntity[] goods = new GoodsEntity[] {good, good1};

    // boolean flag = cashierMapper.save(goods);

    int count = cashierMapper.deleteByExample(Arrays.asList(goods));

    System.out.println(count);

    return false;
  }

  /**
   * 通过条形码查询商品
   */
  // 启用缓存
  @Cacheable(value = "goodsCache", key = "#barcode", unless = "#result == null")
  @Override
  public GoodsEntity findGoodsByBarcode(String barcode) {
    // ApplicationListener.getBean("sqlSessionTemplate");
    System.out.println(cashierMapper);
    System.out.println("barcode : " + barcode);

    GoodsEntity good = cashierMapper.getGoodsByBarcode(barcode);
    // logger.debug("ReportServiceImp" , basicDao==null);
    return good; // this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }

  // 预支付接口 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额
  @Override
  public void prePayment(List<GoodsEntity> goods) {
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
