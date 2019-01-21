package com.kindustry.cashier.service;

import com.kindustry.cashier.entity.Goods;



public interface ICashierService /*extends IBasicService*/ {
  
  public Goods findGoodsByBarcode(String barcode, String value);
 
}
