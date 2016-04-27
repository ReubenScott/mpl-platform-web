package com.soak.framework.jobs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.xml.XmlSqlMapper;

public class CronJob {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  private static final String DEFAULT_ENCODING = "UTF-8";

  private JdbcHandler jdbcHandler;

  public JdbcHandler getJdbcHandler() {
    return jdbcHandler;
  }

  public void setJdbcHandler(JdbcHandler jdbcHandler) {
    this.jdbcHandler = jdbcHandler;
  }

  public void test() {
    logger.debug("12343211234321123432112343211234321");
  /*  String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤");
    sql = sql.replaceAll("@empno", "BI00327");
    Workbook workbook = jdbcHandler.exportExcel(sql);
    String tempFilePath = "D:/考勤2016-01-10.xlsx";

    FileOutputStream fos;
    try {
      fos = new FileOutputStream(new File(tempFilePath));
      workbook.write(fos);
      fos.flush();
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }*/
    
    
  }

}