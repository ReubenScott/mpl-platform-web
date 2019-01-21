package com.kindustry.cashier.service.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kindustry.cashier.entity.Goods;
import com.kindustry.cashier.mapper.CashierMapper;
import com.kindustry.cashier.service.ICashierService;

//import com.kindustry.edw.report.service.IReportService;
//import com.kindustry.framework.service.imp.BasicServiceImp;


@Service
public class CashierServiceImp /*extends BasicServiceImp*/ implements ICashierService {
  
  @Resource
  private CashierMapper cashierDao;

  public Goods findGoodsByBarcode(String barcode, String value) {
//    ApplicationListener.getBean("sqlSessionTemplate");
    
    System.out.println(cashierDao );
    System.out.println("barcode : " +  barcode );
    
    Goods good = cashierDao.getGoodsByBarcode(barcode);
    System.out.println(good);
    
//    logger.debug("ReportServiceImp" , basicDao==null);
    
    return good ;  // this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }



  public CashierMapper getCashierDao() {
    return cashierDao;
  }



  public void setCashierDao(CashierMapper cashierDao) {
    this.cashierDao = cashierDao;
  }
  
  
  
  
}
