package com.soak.framework.service.Imp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.framework.dao.IBasicDao;
import com.soak.framework.service.IBasicService;
import com.soak.framework.util.BrowseTool;

public class BasicServiceImp implements IBasicService {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected IBasicDao basicDao;

  public IBasicDao getBasicDao() {
    return basicDao;
  }

  public void setBasicDao(IBasicDao basicDao) {
    this.basicDao = basicDao;
  }

  public List findByXmlSqlMapper(String xml) {

    return null;
  }


  /**
   * Excel 2007 下载
   * @param fileName
   * @param sql
   * @param params
   */
  public void downloadExcelBySQL(String fileName , String sql, Object... params) {
    HttpServletResponse response = ServletActionContext.getResponse();
    HttpServletRequest request = ServletActionContext.getRequest();
    response.reset(); // 非常重要
    OutputStream os = null;
    try {
      fileName = fileName.trim();
      String userAgent = BrowseTool.checkBrowse(request.getHeader("user-agent"));
      if (userAgent.indexOf("MSIE") >= 0) {
        URLCodec codec = new URLCodec();
        fileName = codec.encode(fileName, "UTF-8");
      } else {
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
      }
      
      // if (fileName.toLowerCase().indexOf(".htm") < 0) {
      // response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      // }

      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileName +".xlsx");
      Workbook workbook = basicDao.createExcelBySQl(sql);
      os = response.getOutputStream();
      workbook.write(os);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (os != null) {
        try {
          os.flush();
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  
}
