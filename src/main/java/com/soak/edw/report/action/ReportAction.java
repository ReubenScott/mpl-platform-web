package com.soak.edw.report.action;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.edw.report.service.ReportService;
import com.soak.framework.action.BaseAction;
import com.soak.framework.cache.EhCacheImpl;

public class ReportAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1327853763505261778L;
  
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private ReportService reportService ;
  
  public ReportService getReportService() {
    return reportService;
  }

  public void setReportService(ReportService reportService) {
    this.reportService = reportService;
  }

  public String getCacheKey() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getCacheName() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isUseCacheData() {
    // TODO Auto-generated method stub
    return false;
  }

  public String onCacheHitResult() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object prepareCache() {
    // TODO Auto-generated method stub
    return null;
  }

  public void supportCache(Object o) {
    // TODO Auto-generated method stub

  }

  public String list() {
    
    reportService.findLicai("每旬理财","2015-12-10");
    EhCacheImpl aa = this.getBean("ehCache");
    System.out.println(aa);
//    ApplicationContext ac = new FileSystemXmlApplicationContext("applicationContext.xml");
    logger.debug("ReportAction : list !");

    return "list";
  }

}
