package com.soak.infoRecod.action;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.framework.action.BaseAction;
import com.soak.framework.service.IBasicService;
import com.soak.framework.thread.ThreadManager;
import com.soak.infoRecod.job.XDBLJob;

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
  

  public void xdbl() {

    XDBLJob job = new XDBLJob();
    Thread t = new Thread(job);
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

  public void xdbl2() {

    Thread t = new Thread() {
      
      public void run() {
        while (!this.isInterrupted()) {
            System.out.println( " thread : " +  Thread.currentThread().getId() + "    " + Thread.currentThread().getName() + " " + this.isInterrupted()  );
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
