package com.soak.framework.service.Imp;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.framework.dao.IBasicDao;
import com.soak.framework.service.IBasicService;

public class BasicServiceImp implements IBasicService {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected IBasicDao basicDao;

  public IBasicDao getBasicDao() {
    return basicDao;
  }

  public void setBasicDao(IBasicDao basicDao) {
    this.basicDao = basicDao;
  }

  /**
   * Excel 2007 下载
   * 
   * @param fileName
   * @param sql
   * @param params
   */
  public Workbook createExcelBySQL(String sql, Object... params) {
    return  basicDao.exportExcel(sql, params);
  }

  /**
   * 获取用户菜单
   */
  public List findMenuByUser(String xml) {

    return basicDao.findUserMenus("");
  }

}
