package com.kindustry.cashier.dao.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kindustry.cashier.dao.CashierMapper;
import com.kindustry.cashier.entity.Goods;
import com.kindustry.orm.dao.BaseMapperBridge;

@Service
public class CashierMapperBridge extends BaseMapperBridge<Goods, CashierMapper> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 
   * @param barcode
   * @return
   * @author kindustry
   */
  public Goods getGoodsByBarcode(String barcode) {
    return super.mapper.getGoodsByBarcode(barcode);
  }

}