package com.soak.framework.service;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.soak.system.model.Menu;


public interface IBasicService {

  // 查询用户菜单
  public List<Menu> findMenuByUser(String uid);
  
  
  // Excel 下载
  public Workbook createExcelBySQL(String title ,String sql, Object... params);
  

  public void deleteEntityBySID(Serializable sid);
  

  public boolean deleteAnnotatedEntity(Object annoEntity);
  
  
}
