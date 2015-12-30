package com.soak.framework.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.soak.framework.cache.ICacheSupport;

public abstract class BaseAction extends ActionSupport implements ICacheSupport, ModelDriven, Preparable {

  private static final long serialVersionUID = 4082024518803969313L;

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  public static final String NOPERMISSION = "NoPermission";

  public static ApplicationContext applicationContext;

//  protected IBasicService baseService;
  
//  public IBasicService getBaseService() {
//    return baseService;
//  }
//
//  public void setBaseService(IBasicService baseService) {
//    this.baseService = baseService;
//  }

  public Object getModel() {
    // TODO Auto-generated method stub
    return null;
  }

  public void prepare() throws Exception {
    // TODO Auto-generated method stub

  }

  /*
   * public static IBaseService getBaseService() { return (IBaseService) applicationContext.getBean("baseService"); }
   */

  /**
   * 获取对象
   * 
   * @param beanName
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> T getBean(String beanName) {
    return (T) applicationContext.getBean(beanName);
  }

  protected void ajaxResponse(String outStr) {
    try {
      HttpServletResponse response = ServletActionContext.getResponse();
      response.setHeader("Cache-Control", "no-cache");
      response.setHeader("Content-Type", "text/json;charset=UTF-8");
      response.setContentType("text/json;charset=UTF-8");

      /*
       * JsonConfig jsonConfig = new JsonConfig(); jsonConfig.registerJsonValueProcessor(Stock.class, new JsonValueProcessor() { public Object processArrayValue(Object value,
       * JsonConfig jsonConfig) { if (value == null) { // return new Date(); } return value; }
       * 
       * public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) { return value + "##修改过的日期"; } });
       */
      response.getOutputStream().write(outStr.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * 以html文本形式 返回响应
   * 
   * @param outStr
   */
  protected void htmlResponse(String outStr) {
    try {
      HttpServletResponse response = ServletActionContext.getResponse();
      response.setHeader("Cache-Control", "no-cache");
      response.setHeader("Content-Type", "text/html; charset=UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      response.getOutputStream().write(outStr.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 上传文件
   * 
   * @param uploadFileUrl
   * @return
   */

  public static String uploadAccessory(File upLoadFile, String uploadFileUrl, String parentFileName) {

    String accessory_table_column = "";
    InputStream is = null;
    OutputStream os = null;
    try {
      String[] urls = uploadFileUrl.split("\\.");
      String name = uploadFileUrl.substring(uploadFileUrl.lastIndexOf("\\") + 1);
      name = name.substring(0, name.lastIndexOf("."));
      String suffix = urls[urls.length - 1];
      accessory_table_column = parentFileName + "\\" + name + "_" + new Date().getTime() + "." + suffix;
      String fileDirectory = ServletActionContext.getServletContext().getRealPath("UploadFiles");
      String fullNewFilePath = fileDirectory + "\\" + accessory_table_column;

      if (upLoadFile.length() == 0) {
        File emptyFile = new File(fullNewFilePath);
        if (!emptyFile.exists()) {
          emptyFile.createNewFile();
        }
      } else {
        is = new BufferedInputStream(new FileInputStream(upLoadFile));
        os = new BufferedOutputStream(new FileOutputStream(fullNewFilePath));
        byte[] bytes = new byte[1024];
        int temp = 0;
        while ((temp = is.read(bytes)) != -1) {
          os.write(bytes, 0, temp);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (os != null) {
        try {
          os.flush();
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return accessory_table_column.replace("\\", "/");
  }

  /**
   * 下载文件
   * 
   */
  public static void downLoadAcesssory(String file) {
    HttpServletResponse response = ServletActionContext.getResponse();
    response.reset(); // 非常重要
    InputStream is = null;
    OutputStream os = null;
    try {
      String fileDirectory = ServletActionContext.getServletContext().getRealPath("UploadFiles");
      String tableFileName = ServletActionContext.getRequest().getParameter("fileUrl");
      tableFileName = new String(tableFileName.getBytes("iso-8859-1"), "utf-8");
      String fileUrl = fileDirectory + "\\" + file + "\\" + tableFileName;
      String uploadFileName = tableFileName.substring(0, tableFileName.indexOf("_")) + tableFileName.substring(tableFileName.lastIndexOf("."));

      response.setContentType("application/x-msdownload;charset='utf-8'");
      response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(uploadFileName.getBytes("gbk"), "iso-8859-1") + "");

      is = new BufferedInputStream(new FileInputStream(fileUrl));
      os = response.getOutputStream();
      byte[] bytes = new byte[1024];
      int temp = 0;
      while ((temp = is.read(bytes)) != -1) {
        os.write(bytes, 0, temp);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
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
