package com.soak.framework.service.Imp;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.framework.dao.IBasicDao;
import com.soak.framework.dao.imp.BasicDaoImp;
import com.soak.framework.service.IBasicService;
import com.soak.system.model.Menu;

public class BasicServiceImp implements IBasicService {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected IBasicDao basicDao = new BasicDaoImp();

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
  public Workbook createExcelBySQL(String title , String sql, Object... params) {
    return  basicDao.exportExcel(null,title ,sql, params);
  }

  /**
   * 获取用户菜单
   */
  public List<Menu> findMenuByUser(String xml) {
    Menu menu = new Menu();
    return basicDao.findByAnnotatedSample(menu);
  }

}
