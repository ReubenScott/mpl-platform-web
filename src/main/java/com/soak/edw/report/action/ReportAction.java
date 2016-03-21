package com.soak.edw.report.action;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.edw.report.service.IReportService;
import com.soak.framework.action.BaseAction;
import com.soak.framework.cache.EhCacheImpl;
import com.soak.framework.xml.XmlSqlMapper;

public class ReportAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1327853763505261778L;

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private IReportService reportService;

  public IReportService getReportService() {
    return reportService;
  }

  public void setReportService(IReportService reportService) {
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

    List list = reportService.findLicai("每旬理财", "2015-12-10");

    EhCacheImpl aa = this.getBean("ehCache");
    System.out.println(aa);
    // ApplicationContext ac = new FileSystemXmlApplicationContext("applicationContext.xml");
    logger.debug("ReportAction : list !", list);

    return "list";
  }

  public void downloadExcel() {
    String fileName = "理财";
    String sql = XmlSqlMapper.getInstance().getPreparedSQL("高管活期积数, 余额");
    sql = sql.replaceAll("@startDate", "2015-11-21");
    sql = sql.replaceAll("@endDate", "2015-11-30");
    
    reportService.downloadExcelBySQL(fileName, sql);
  }

  public static void main(String[] args) {


    byte[] c = "^".getBytes();
    for (byte cc : c) {
      System.out.println("0x0" + Integer.toHexString(cc));
    }

  }

}
