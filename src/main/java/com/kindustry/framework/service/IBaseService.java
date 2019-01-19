package com.kindustry.framework.service;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.kindustry.framework.dao.IBaseDao;
import com.kindustry.system.model.Menu;

public interface IBaseService {

  public IBaseDao getBaseDao();

  public void setBaseDao(IBaseDao baseDao);

  // 查询用户菜单
  public List<Menu> findMenuByUser(String uid);

  // Excel 下载
  public Workbook createExcelBySQL(String title, String sql, Object... params);

  public boolean deleteEntityBySID(Class entity, Serializable sid);

  public boolean deleteAnnotatedEntity(Object annoEntity);

}