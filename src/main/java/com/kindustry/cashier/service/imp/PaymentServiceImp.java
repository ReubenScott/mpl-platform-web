package com.kindustry.cashier.service.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kindustry.cashier.dao.CashierMapper;
import com.kindustry.cashier.entity.CommodityEntity;
import com.kindustry.cashier.service.IPaymentService;

@Service
public class PaymentServiceImp implements IPaymentService {

  @Resource
  private CashierMapper cashierMapper;

  @Override
  public CommodityEntity getBean(String name) {
    CommodityEntity good = cashierMapper.getGoodsByBarcode(name);
    System.out.println(good);
    return good;
  }
}
