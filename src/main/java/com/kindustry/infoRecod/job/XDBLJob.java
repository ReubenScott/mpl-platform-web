package com.kindustry.infoRecod.job;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kindustry.framework.jdbc.core.JdbcTemplate;

/**
 * <p>
 * 需要进行调度工作的任务
 * </p>
 */
public class XDBLJob implements Runnable {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public void run() {
    // 1. get 文件
//    FtpZilla fa = new FtpZilla("32.137.32.41", 21, "sjxf", "sjxf");
    String today = new SimpleDateFormat("yyyyMMdd").format(new Date()); // new Date()为获取当前系统时间
    String remoteFilePath = "/home/sjxf/data/data_ykdt/" + today + ".zip";
    // fa.downFile("D:/home", filePath);

    // 2. zip 解压
    // FileUtil.unZip("D:/home/" + today + ".zip", "D:\\home\\");
    // 3. 文件入库
    JdbcTemplate jdbc = JdbcTemplate.getInstance();
    File dir = new File("D:/home/" + today);

    //    for (File delfile : dir.listFiles()) {
//      System.out.println(delfile.getName());
//    jdbc.truncateTable("edw", "YKJD_CUST_ENT");
//    jdbc.truncateTable("edw", "YKJD_CUST_INFO");
//    jdbc.truncateTable("edw", "YKJD_CUST_PER");
    jdbc.truncateTable("edw", "YKJD_LN_DUEBILL");
//    jdbc.truncateTable("edw", "YKJD_SS_DICT");
    
//    jdbc.loadCsvFile("edw","YKJD_CUST_ENT", "D:/home/" + today + "/CUST_ENT.del" , (char) 44);
//    jdbc.loadCsvFile("edw","YKJD_CUST_INFO", "D:/home/" + today + "/CUST_INFO.del" , (char) 44);
//    jdbc.loadCsvFile("edw","YKJD_CUST_PER", "D:/home/" + today + "/CUST_PER.del" , (char) 44);
    jdbc.loadDelFile("edw","YKJD_LN_DUEBILL", "D:/home/" + today + "/LN_DUEBILL.del" , (char) 44);
//    jdbc.loadCsvFile("edw","YKJD_SS_DICT", "D:/home/" + today + "/SS_DICT.del" , (char) 44);
      
    // select * from edw.ykjd_ss_dict ;
    // all the run() method's code goes here
    // do some work
    System.out.println(" thread : " + Thread.currentThread().getId() + "    " + Thread.currentThread().getName() + " " + Thread.currentThread().isInterrupted());
    Thread.yield(); // let another thread have some time perhaps to stop this one.
    if (Thread.currentThread().isInterrupted()) {
      logger.debug(" thread : " + Thread.currentThread().getId() + "    " + Thread.currentThread().getName() + " " + Thread.currentThread().isInterrupted());
    }
    // do some more work
  }

  /**
   * 线程实现
   */
  public void run2() {

    while (!Thread.currentThread().isInterrupted()) {
      // 业务流程
      System.out.println(" thread : " + Thread.currentThread().getId() + "    " + Thread.currentThread().getName() + " " + Thread.currentThread().isInterrupted());
      Thread.yield(); // let another thread have some time perhaps to stop this one.
    }
  }

}
