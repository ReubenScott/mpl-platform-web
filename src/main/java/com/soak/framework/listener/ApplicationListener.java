package com.soak.framework.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.soak.framework.action.BaseAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ApplicationListener implements ServletContextListener {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Timer timer;

  /**
   * web 应用 初始化操作
   */
  public void contextInitialized(ServletContextEvent event) {
    logger.debug("ApplicationListener  :  contextInitialized   ....................");

    // Spring 上下文 初始化
    ServletContext context = event.getServletContext();
    BaseAction.applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

    this.schedule();

  }

  // 定时任务
  private void schedule() {

    long interval = 1000 * 60 * 60 * 24; // 隔一天

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 2);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    Calendar d = Calendar.getInstance();

    logger.debug("TimerTask calendar : " + calendar.getTime().toLocaleString());
    Date current = new Date();
    logger.debug("TimerTask Date : " + current.toLocaleString());
    logger.debug("TimerTask calendar : " + d.getTime().toLocaleString());

    timer = new Timer();
    TimerTask task = new TimerTask() {
      int i = 0;

      public void run() {
        i++;
        logger.debug(" timer[" + i + "]");
      }
    };

    // 定时任务
    timer.schedule(task, calendar.getTime(), interval);

  }

  /**
   * web 应用 关闭
   */
  public void contextDestroyed(ServletContextEvent sce) {
    BaseAction.applicationContext = null;

    logger.debug("ApplicationListener  :  contextDestroyed   ....................");
    if (timer != null) {
      timer.cancel();
    }
  }
}