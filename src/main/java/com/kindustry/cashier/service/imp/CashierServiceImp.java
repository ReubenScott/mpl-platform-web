package com.kindustry.cashier.service.imp;

import java.util.List;

import javax.annotation.Resource;

import net.sf.ehcache.Element;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import com.kindustry.cashier.entity.Goods;
import com.kindustry.cashier.mapper.CashierMapper;
import com.kindustry.cashier.service.ICashierService;
import com.kindustry.cashier.vo.DeviceInfo;
import com.kindustry.common.util.JsonUtil;
import com.kindustry.framework.service.imp.BaseServiceImp;

//import com.kindustry.edw.report.service.IReportService;
//import com.kindustry.framework.service.imp.BasicServiceImp;


@Service("cashierService")
public class CashierServiceImp extends BaseServiceImp implements ICashierService {
  
  @Resource
  private CashierMapper cashierDao;


  @Override
  public boolean regester(DeviceInfo deviceinfo) {
    // TODO Auto-generated method stub
    return false;
  }

  // 通过条形码查询商品
  @Cacheable(value="contentCache",key="#barcode" , unless="#result == null")  // 启用缓存 
  public Goods findGoodsByBarcode(String barcode) {
//    ApplicationListener.getBean("sqlSessionTemplate");
    System.out.println(cashierDao );
    System.out.println("barcode : " +  barcode );
    
    Goods good = cashierDao.getGoodsByBarcode(barcode);
//    logger.debug("ReportServiceImp" , basicDao==null);
    return good ;  // this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }



  // 预支付接口 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额      
  public void prePayment(List<Goods> goods) {
//    ApplicationListener.getBean("sqlSessionTemplate");
    System.out.println(cashierDao );

//    Goods element = super.getCacheBean("contentCache", barcode);
//     if(element!= null ){
//        System.out.println(JsonUtil.toJSONString(element));
//     }
     
     List keys = super.getCacheKeys("token") ;
     System.out.println(keys.size());
     for (Object obj : keys){
       System.out.println(obj);
     }
//    logger.debug("ReportServiceImp" , basicDao==null);
  }


  // 支付接口 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额      
  public boolean payment() {
    // 方式一: 现金支付   Cash payment

//    Goods element = super.getCacheBean("contentCache", barcode);
//     if(element!= null ){
//        System.out.println(JsonUtil.toJSONString(element));
//     }     
     List keys = super.getCacheKeys("token") ;
     System.out.println(keys.size());
     for (Object obj : keys){
       System.out.println(obj);
     }
     
    return true ;
  }


  
}
