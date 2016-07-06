package com.soak.framework.jobs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.soak.common.terminal.FtpZilla;
import com.soak.common.terminal.UserAuthInfo;
import com.soak.framework.dao.IBasicDao;
import com.soak.framework.date.DateUtil;
import com.soak.framework.thread.ThreadPool;
import com.soak.framework.xml.XmlSqlMapper;

public class CronJob {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  private IBasicDao basicDao ;

  public IBasicDao getBasicDao() {
    return basicDao;
  }

  public void setBasicDao(IBasicDao basicDao) {
    this.basicDao = basicDao;
  }

  public void test() {
//    System.out.println(System.getProperty("java.io.tmpdir"));
//    System.out.println(System.getenv("TMP"));

//    ThreadPool threadPool =  ThreadPool.getInstance() ;
//    threadPool.push( new Thread() {
//      public void run() {
//        logger.debug(basicDao + " 12343211234321123432112343211234321");
//      }
//     });

  }

  
  /***
   * 
   * 积分商城导数
   * 
   */
  public void exportDataForIntegralMall() {
    System.out.println(System.getProperty("java.io.tmpdir"));
    System.out.println(System.getenv("TMP"));
    
    String tmpDir = System.getProperty("java.io.tmpdir");

    Date monthEndETLDT = DateUtil.getLastDayOfMonth(DateUtil.addMonths(DateUtil.getCurrentDateTime(), -1));
    String exportDate = DateUtil.formatShortDate(monthEndETLDT) ;
    System.out.println(monthEndETLDT);
    
    String dbalias = "db94" ;

    // 检查 跑批状态
    String checkSql = "select curdate , procstep from etl.systempara ";
    Map checkMap = basicDao.queryOneAsMap(dbalias, checkSql);

    if (checkMap != null) {
      Date etlDate = (Date) checkMap.get("curdate");
      String procstep = (String) checkMap.get("procstep");

      // 检查 跑批状态
      float diff = DateUtil.timeDiff(monthEndETLDT, etlDate);
      if (diff > 0 || (diff == 0 && "DONE".equals(procstep))) {
        String sql = XmlSqlMapper.getInstance().getPreparedSQL("卡存款月积数");
        sql = sql.replaceAll("@statdate", exportDate);
        logger.debug(sql);
        String filePath = tmpDir + "/CUST_INFO_"+exportDate+".del";
        basicDao.exportCSV(dbalias,filePath, "GBK", (char) 44, sql);

        // 2. FTP 上传
        boolean updateFlag = false ;
        while(updateFlag != true){
          try {
            UserAuthInfo userAuthinfo = new UserAuthInfo("32.137.32.116", 21, "ftpuser", "ftpuser");
            String basedir = "/home/userbalance";
            String filename = "CUST_INFO_"+exportDate+".del";
            InputStream is = new FileInputStream(filePath);
            //  上传
            updateFlag = FtpZilla.uploadFile(userAuthinfo, is, basedir, filename);
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
        }

      }
    }

  }

  public void writeFile() {

    String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤");
    sql = sql.replaceAll("@empno", "BI00194");

    String tempFilePath = "D:/20160110.del";
    // writeFile(tempFilePath, "D:/20160110.del");
    basicDao.exportCSV(tempFilePath, "GBK", (char) 44, sql);

    // try {
    // File f = new File(filePathAndName);
    // if (!f.exists()) {
    //      
    // f.createNewFile();
    // }
    // //定义编码
    // OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
    // BufferedWriter writer = new BufferedWriter(write);
    // writer.write(fileContent);
    // writer.close();
    // } catch (Exception e) {
    // System.out.println("写文件内容操作出错");
    // e.printStackTrace();
    // }
  }
  
  public static void main(String args[]){
//    String[] locations = {"bean1.xml", "bean2.xml", "bean3.xml"};
//    "file:src/applicationContext.xml"
    
//    D:\workspace\epl-edw-web\src\main\resources
//     String[] configs = {"D:/workspace/epl-edw-web/src/main/resources/applicationContext.xml"
//         , "D:/workspace/epl-edw-web/src/main/resources/modules/framework-bean.xml"    
//         , "D:/workspace/epl-edw-web/src/main/resources/modules/framework-bean.xml"    
//     }; 
//     ApplicationContext context = new FileSystemXmlApplicationContext(configs);
////     XmlWebApplicationContext context = new XmlWebApplicationContext();  
////     context.setConfigLocations(configs);  
//     
//     JdbcHandler jdbc = (JdbcHandler)context.getBean("jdbcHandler");
//     System.out.println(jdbc);
    
    CronJob job = new CronJob();
    job.exportDataForIntegralMall();
    
  }

}