package com.kindustry.edw.report.service.imp;

import java.util.List;

import com.kindustry.edw.report.service.IReportService;
import com.kindustry.framework.service.imp.BasicServiceImp;

public class ReportServiceImp extends BasicServiceImp implements IReportService {
   
  public List<?> findLicai(String sqlName, String value) {
    
    
    logger.debug("ReportServiceImp" , basicDao==null);
    
    return this.basicDao.findByXmlSqlMapper(null,sqlName, value);
  }
  
  
  
  
}
