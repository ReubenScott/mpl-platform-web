package com.kindustry.cashier.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kindustry.cashier.dao.bridge.CashierMapperBridge;
import com.kindustry.cashier.entity.CommodityEntity;
import com.kindustry.cashier.service.ICashierService;
import com.kindustry.framework.service.imp.BaseServiceImp;

@Service
public class CashierServiceImp extends BaseServiceImp implements ICashierService {

  @Autowired
  private CashierMapperBridge cashierMapper;

  public boolean regester() {
    // TODO Auto-generated method stub
    CommodityEntity good = new CommodityEntity();
    good.setBarcode("6927241400304");
    good.setTitle("贵人车马");

    CommodityEntity good1 = new CommodityEntity();
    good1.setTitle("梳子");
    good1.setCargoNo("1221");

    CommodityEntity[] goods = new CommodityEntity[] {good, good1};

    boolean flag = cashierMapper.save(goods);

    // int count = cashierMapper.deleteByExample(Arrays.asList(goods), new String[] {"cargoNo", "title"});
    // System.out.println(count);

    // int count2 = cashierMapper.deleteByPk(goods);

    // System.out.println(count2);

    return false;
  }

  /**
   * 通过条形码查询商品
   */
  // 启用缓存
  @Cacheable(value = "goodsCache", key = "#barcode", unless = "#result == null")
  @Override
  public CommodityEntity findGoodsByBarcode(String barcode) {
    // ApplicationListener.getBean("sqlSessionTemplate");
    System.out.println(cashierMapper);
    System.out.println("barcode : " + barcode);

    CommodityEntity good = cashierMapper.getGoodsByBarcode(barcode);
    // logger.debug("ReportServiceImp" , basicDao==null);
    return good; // this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }

  // 预支付接口 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额
  @Override
  public void prePayment(List<CommodityEntity> goods) {
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
