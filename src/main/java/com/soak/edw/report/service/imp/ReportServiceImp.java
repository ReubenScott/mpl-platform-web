package com.soak.edw.report.service.imp;

import java.util.List;

import com.soak.edw.report.service.IReportService;
import com.soak.framework.service.imp.BasicServiceImp;

public class ReportServiceImp extends BasicServiceImp implements IReportService {
   
  public List<?> findLicai(String sqlName, String value) {
    
    
    logger.debug("ReportServiceImp" , basicDao==null);
    
    return this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }
  
  
  
  
}
