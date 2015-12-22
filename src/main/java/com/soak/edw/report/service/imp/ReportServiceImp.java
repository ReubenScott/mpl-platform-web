package com.soak.edw.report.service.imp;

import java.util.List;

import com.soak.edw.report.service.ReportService;
import com.soak.framework.service.Imp.BasicServiceImp;

public class ReportServiceImp extends BasicServiceImp implements ReportService {
   
  public List<?> findLicai(String sqlName, String value) {
    
    
    logger.debug("ReportServiceImp" , basicDao==null);
    
    return this.basicDao.findByXmlSqlMapper(sqlName, value);
  }
  
  
  
  
}
