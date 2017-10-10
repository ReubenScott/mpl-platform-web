package com.kindustry.framework.service.imp;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kindustry.framework.dao.IBasicDao;
import com.kindustry.framework.dao.imp.BasicDaoImp;
import com.kindustry.framework.service.IBasicService;
import com.kindustry.system.model.Menu;

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

  
  /**
   * 
   * @param sid
   */
  public void deleteEntityBySID(Serializable sid) {
    // TODO Auto-generated method stub

  }
  
  /**
   * 
   * @param sid
   */
  public boolean deleteAnnotatedEntity(Object annoEntity){
    return basicDao.deleteAnnotatedEntity(annoEntity);
  }
  
  
}
