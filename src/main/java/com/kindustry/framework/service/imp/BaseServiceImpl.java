package com.kindustry.framework.service.imp;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Workbook;

import com.kindustry.framework.dao.IBaseDao;
import com.kindustry.framework.service.IBaseService;
import com.kindustry.system.model.Menu;

public class BaseServiceImpl implements IBaseService {

  @Resource
  protected IBaseDao baseDao;

  public IBaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(IBaseDao baseDao) {
    this.baseDao = baseDao;
  }

  /**
   * Excel 2007 下载
   * 
   * @param fileName
   * @param sql
   * @param params
   */
  public Workbook createExcelBySQL(String title, String sql, Object... params) {
//    return basicDao.exportExcel(null, title, sql, params);
    return null ;
  }

  /**
   * 获取用户菜单
   */
  public List<Menu> findMenuByUser(String xml) {
    Menu menu = new Menu();
//    return basicDao.findByAnnotatedSample(menu);
    return null ;
  }

  /**
   * 
   * @param sid
   */
  public boolean deleteEntityBySID(Class entity, Serializable sid) {
//    return basicDao.deleteEntityBySID(entity, sid);
    return false ;
  }

  /**
   * 
   * @param sid
   */
  public boolean deleteAnnotatedEntity(Object annoEntity) {
//    return basicDao.deleteAnnotatedEntity(annoEntity);
    return false ;
  }

}