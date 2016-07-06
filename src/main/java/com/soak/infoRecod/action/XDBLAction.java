package com.soak.infoRecod.action;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.framework.action.BaseAction;
import com.soak.framework.service.IBasicService;
import com.soak.framework.thread.ThreadManager;
import com.soak.infoRecod.job.XDBLJob;
import com.soak.infoRecod.service.InfoRecordService;

public class XDBLAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1327853763505261778L;

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private IBasicService basicService;

  public IBasicService getBasicService() {
    return basicService;
  }

  public void setBasicService(IBasicService basicService) {
    this.basicService = basicService;
  }
  
  
  public void xdbl(){

    InfoRecordService infoService = this.getBean("infoService");
    infoService.hello();
  }

  public void xdbl2() {
    basicService.createExcelBySQL("1","SELECT COUNT(1) FROM edw.ykjd_ln_duebill ");

    

//    XDBLJob job = new XDBLJob();
    // 通过ProxyFactoryBean获取IComputer接口实现类的实例
    XDBLJob job = this.getBean("job");
    Thread t = new Thread(job);
    t.setName("XDBL");
    ThreadManager threadManager = ThreadManager.getInstance();
    threadManager.push(t);
    t.start();

    // 创建JSONObject对象
    JSONObject json = new JSONObject();
    // 向json中添加数据
    json.put("status", "start");
    // json.put("thread_id", t.getId());

    super.ajaxResponse(json.toString());
  }

  public void xdbl23() {

    Thread t = new Thread() {

      public void run() {
        while (!this.isInterrupted()) {
          System.out.println(" thread : " + Thread.currentThread().getId() + "    " + Thread.currentThread().getName() + " " + this.isInterrupted());
        }
      }
    };

    t.setName("XDBL");
    ThreadManager threadManager = ThreadManager.getInstance();
    threadManager.push(t);
    t.start();

    // 创建JSONObject对象
    JSONObject json = new JSONObject();
    // 向json中添加数据
    json.put("status", "start");
    json.put("thread_id", t.getId());

    super.ajaxResponse(json.toString());
  }

  public void xdblStop() {

    ThreadManager threadManager = ThreadManager.getInstance();
    threadManager.stop("XDBL");
    System.out.println("222222222222222222222222222222222222");

    // 创建JSONObject对象
    JSONObject json = new JSONObject();
    // 向json中添加数据
    json.put("status", "stop");

    super.ajaxResponse(json.toString());

  }

}
