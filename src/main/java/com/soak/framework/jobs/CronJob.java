package com.soak.framework.jobs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.soak.common.terminal.FtpZilla;
import com.soak.common.terminal.SecureCRT;
import com.soak.common.terminal.UserAuthInfo;
import com.soak.framework.date.DateUtil;
import com.soak.framework.jdbc.DynamicDataSource;
import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.xml.XmlSqlMapper;

public class CronJob {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  private static final String DEFAULT_ENCODING = "UTF-8";

  private JdbcHandler jdbcHandler = JdbcHandler.getInstance();

  public JdbcHandler getJdbcHandler() {
    return jdbcHandler;
  }

  public void setJdbcHandler(JdbcHandler jdbcHandler) {
    this.jdbcHandler = jdbcHandler;
  }

  public void test() {
    logger.debug("12343211234321123432112343211234321");
    /*
     * String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤"); sql =
     * sql.replaceAll("@empno", "BI00327"); Workbook workbook =
     * jdbcHandler.exportExcel(sql); String tempFilePath =
     * "D:/考勤2016-01-10.xlsx";
     * 
     * FileOutputStream fos; try { fos = new FileOutputStream(new
     * File(tempFilePath)); workbook.write(fos); fos.flush(); fos.close(); }
     * catch (FileNotFoundException e) { e.printStackTrace(); } catch
     * (IOException e) { e.printStackTrace(); }
     */

  }
  
  
  public void writeFile() {

    String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤");
    sql = sql.replaceAll("@empno", "BI00194");
    
    String tempFilePath = "D:/20160110.del"; 
//    writeFile(tempFilePath, "D:/20160110.del");
    jdbcHandler.exportCSV(tempFilePath, "GBK", (char)44, sql);
    
    
//    try {
//     File f = new File(filePathAndName);
//     if (!f.exists()) {
//      
//      f.createNewFile();
//     }
//     //定义编码
//     OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
//     BufferedWriter writer = new BufferedWriter(write);
//     writer.write(fileContent);
//     writer.close();
//    } catch (Exception e) {
//     System.out.println("写文件内容操作出错");
//     e.printStackTrace();
//    }
   }
  

  public void exportDataForIntegralMall() {
    try {
      // UserAuthInfo userAuthinfo = new UserAuthInfo("32.137.32.41", 21,"sjxf", "sjxf");

//      String sql = XmlSqlMapper.getInstance().getPreparedSQL("每旬理财");
      
//      sql = sql.replaceAll("@statdate", DateUtil.getLastDayOfMonth("2016-04-01"));
//      Workbook workbook = jdbcHandler.exportExcel(sql);
//      String tempFilePath = "D:/考勤2016-01-10.xlsx";
      
//      String sql = "SELECT T1.STATDATE     as  STATDATE " 
//        + ",T1.CLTNBR       as  CLTNBR "
//        + ",T1.CLTNM      as  CLTNM "
//        + ",'3206231987762x'   as  SA_CERT_ID "
//        + ",T1.MTHAUB as  MTHAUB "
//        + ",T1.BRNNBR  as  BRNNBR "
//        + " FROM EDW.S_DEP_SA_ACCOUNT_D           T1 "
//        + " fetch first 10 rows only "  ;
      String sql = XmlSqlMapper.getInstance().getPreparedSQL("卡存款月积数");
      sql = sql.replaceAll("@statdate", "2016-04-30");
      
      String tempFilePath = "D:/20160110.del"; 
//      writeFile(tempFilePath, "D:/20160110.del");
      jdbcHandler.exportCSV(tempFilePath , "GBK" , (char)44, sql);
      
      
//      try {
//        fos = new FileOutputStream(new File(tempFilePath));
//        workbook.write(fos);
//        fos.flush();
//        fos.close();
//      } catch (FileNotFoundException e) {
//        e.printStackTrace();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
      
      //2. FTP 上传
      UserAuthInfo userAuthinfo = new UserAuthInfo("32.137.32.116", 21, "ftpuser", "ftpuser");
      String basedir = "/home/userbalance/文档";
      String filename = "市民-2016-04-10-0.log";
      InputStream is = new FileInputStream("D:/logs/myApp.error-2016-04-10-0.log");
      FtpZilla.uploadFile(userAuthinfo, is, basedir, filename);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

}