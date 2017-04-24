package com.soak.infoInquiry.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.soak.common.mail.MailBean;
import com.soak.common.mail.MailService;
import com.soak.framework.action.BaseAction;
import com.soak.framework.cache.EhCacheImpl;
import com.soak.framework.service.IBasicService;
import com.soak.framework.xml.XmlSqlMapper;


/***
 * Information Inquiry System
 * @author suse
 *
 */
public class SelfServiceAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1327853763505261778L;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private IBasicService basicService;


  public IBasicService getBasicService() {
    return basicService;
  }

  public void setBasicService(IBasicService basicService) {
    this.basicService = basicService;
  }

  public String getCacheKey() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getCacheName() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isUseCacheData() {
    // TODO Auto-generated method stub
    return false;
  }

  public String onCacheHitResult() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object prepareCache() {
    // TODO Auto-generated method stub
    return null;
  }

  public void supportCache(Object o) {
    // TODO Auto-generated method stub

  }
  
  public String getMenus(){

    List menus  = basicService.findMenuByUser("");
    
    System.out.println(menus);
    
    return "ss";
  }

  public String licai() {

    EhCacheImpl aa = this.getBean("ehCache");
    // ApplicationContext ac = new FileSystemXmlApplicationContext("applicationContext.xml");

    String sql = XmlSqlMapper.getInstance().getPreparedSQL("每旬理财");
    sql = sql.replaceAll("@date", "2016-01-10");

    logger.debug("sql :", sql);

    Workbook workbook = basicService.createExcelBySQL("1", sql);

    ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
    String tempFilePath = sc.getRealPath("/temp") + "/每旬理财2016-01-10.xlsx";

    FileOutputStream fos;
    try {
      fos = new FileOutputStream(new File(tempFilePath));
      workbook.write(fos);
      fos.flush();
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      logger.error("tempFilePath : ", tempFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }

    
    // 发邮件
    MailBean mail = new MailBean();
    mail.setTitle("每旬理财2016-01-10");
    mail.setAttachName("每旬理财2016-01-10.xlsx");
    mail.setTargetMail("chenjun12@beyondsoft.com");
    mail.setContent("每旬理财2016-01-10");
    logger.debug(tempFilePath);
    mail.setAttachPath(tempFilePath);

    // bean.setTargetName("收件人");
    MailService.sendMail(mail);

    return "list";
  }

  public void Ajax_Func2() {

    // 定义JSON字符串
    String jsonStr = "{\"id\": 2," + " \"title\": \"json title\", " + "\"config\": {" + "\"width\": 34," + "\"height\": 35," + "}, \"data\": ["
        + "\"JAVA\", \"JavaScript\", \"PHP\"" + "]}";
    // 创建JSONObject对象
    JSONObject json = new JSONObject();
    // 向json中添加数据
    json.put("name", "wanglihong");
    json.put("age", 12);
    json.put("email", "sdsd@hotmlsom.com");
    
    // 创建JSONArray数组，并将json添加到数组
    JSONArray array = new JSONArray();
//    array.put(json);
//    array.put(json);
//    array.put(json);
    
    super.ajaxResponse(array.toString());
  }


  public void downloadExcel() {
    String fileName = "理财";
    String sql = XmlSqlMapper.getInstance().getPreparedSQL("高管活期积数, 余额");
    sql = sql.replaceAll("@startDate", "2015-11-21");
    sql = sql.replaceAll("@endDate", "2015-11-30");
    
    this.downloadExcel(fileName, basicService.createExcelBySQL("1" , sql));
    
  }
}
