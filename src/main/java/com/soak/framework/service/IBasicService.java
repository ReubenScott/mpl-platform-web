package com.soak.framework.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

public interface IBasicService {

  public List findByXmlSqlMapper(String sqlName);
  
  public void downloadExcelBySQL(String fileName , String sql, Object... params);
  
}
