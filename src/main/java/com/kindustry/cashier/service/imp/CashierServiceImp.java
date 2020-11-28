package com.kindustry.cashier.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kindustry.cashier.entity.Goods;
import com.kindustry.cashier.mapper.CashierMapper;
import com.kindustry.cashier.service.ICashierService;

@Service
public class CashierServiceImp /* extends BasicServiceImp */implements ICashierService {

  @Autowired
  private CashierMapper cashierDao;

  public Goods findGoodsByBarcode(String barcode, String value) {
    // ApplicationListener.getBean("sqlSessionTemplate");

    System.out.println(cashierDao);
    System.out.println("barcode : " + barcode);

    Goods good = cashierDao.getGoodsByBarcode(barcode);
    System.out.println(good);

    // logger.debug("ReportServiceImp" , basicDao==null);

    return good; // this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }

  public CashierMapper getCashierDao() {
    return cashierDao;
  }

  public void setCashierDao(CashierMapper cashierDao) {
    this.cashierDao = cashierDao;
  }

}
