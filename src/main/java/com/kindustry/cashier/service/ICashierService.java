package com.kindustry.cashier.service;

import java.util.List;

import com.kindustry.cashier.entity.Goods;
import com.kindustry.framework.service.IBaseService;



public interface ICashierService extends IBaseService {
  
  // 通过条形码查询商品
  public Goods findGoodsByBarcode(String barcode);
 
  // 预支付接口 客户端提供参数： 商品明细： 会员号， 商品条码 , 数量 ，金额      
  public void prePayment(List<Goods> goods) ;
  
}
