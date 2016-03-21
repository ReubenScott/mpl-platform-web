package com.soak.infoRecod.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.soak.common.ftp.FtpZilla;
import com.soak.common.io.FileUtil;

/**
 * <p>
 * 需要进行调度工作的任务
 * </p>
 */
public class XDBLJob implements  Runnable {

  private volatile Thread myThread;


  /**
   * 停止线程
   */
  public void stop() {
    Thread tmpThread = myThread;
    myThread = null;
    if (tmpThread != null) {
      tmpThread.interrupt();
    }
  }
  

  public void run() {
    try {
      // 1.  get 文件 
      FtpZilla fa = new FtpZilla("32.137.32.41", 21, "sjxf", "sjxf");
      String today = new SimpleDateFormat("yyyyMMdd").format(new Date()) ;  // new Date()为获取当前系统时间      
      String filePath = "/home/sjxf/data/data_ykdt/" + today +".zip" ;
      fa.downFile("D:/home" , filePath );
      
      // 2. zip 解压
      FileUtil.unZip("D:/home/"+ today +".zip", "D:\\home\\");      
      // 3. 文件入库
//      select * from edw.ykjd_ss_dict ;
      // all the run() method's code goes here
      // do some work
      System.out.println(" thread : " + myThread + "  " + Thread.currentThread().getId() + "    " + Thread.currentThread().getName() + " " + Thread.currentThread().isInterrupted());
      Thread.yield();  // let another thread have some time perhaps to stop this one.
      if (Thread.currentThread().isInterrupted()) {
        throw new InterruptedException("Stopped by ifInterruptedStop()");
      }
      // do some more work
    } catch (Throwable t) {
      // log/handle all errors here
    }
  }


  /**
   * 线程实现
   */
  public void run2() {

    while (!Thread.currentThread().isInterrupted()) {
      // 业务流程
      System.out.println(" thread : " + myThread + "  " + Thread.currentThread().getId() + "    " + Thread.currentThread().getName() + " " + Thread.currentThread().isInterrupted());
      Thread.yield();  // let another thread have some time perhaps to stop this one.
    }
  }

}
