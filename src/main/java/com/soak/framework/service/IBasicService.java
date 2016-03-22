package com.soak.framework.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;


public interface IBasicService {

  // 查询用户菜单
  public List findMenuByUser(String uid);
  
  
  // Excel 下载
  public Workbook createExcelBySQL(String sql, Object... params);
  
}
