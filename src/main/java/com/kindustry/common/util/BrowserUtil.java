package com.kindustry.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

public class BrowserUtil {

  /** java获取客户端 */
  public static String getPlatform() {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    /**
     * User Agent中文名为用户代理，简称 UA，它是一个特殊字符串头，使得服务器 能够识别客户使用的操作系统及版本、CPU
     * 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等
     */
    String agent = request.getHeader("user-agent");
    // 客户端类型常量
    String type = "";
    if (agent.contains("iPhone") || agent.contains("iPod") || agent.contains("iPad")) {
      type = "ios";
    } else if (agent.contains("Android") || agent.contains("Linux")) {
      type = "apk";
    } else if (agent.indexOf("micromessenger") > 0) {
      type = "wx";
    } else {
      type = "pc";
    }
    return type;
  }

  public static String getDeviceInfo() {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();

    String pattern = "^Mozilla/\\d\\.\\d\\s+\\(+.+?\\)";
    String pattern2 = "\\(+.+?\\)";
    Pattern r = Pattern.compile(pattern);
    Pattern r2 = Pattern.compile(pattern2);

    String userAgent = request.getHeader("User-Agent");
    Matcher m = r.matcher(userAgent);
    String result = null;
    if (m.find()) {
      result = m.group(0);
    }

    Matcher m2 = r2.matcher(result);
    if (m2.find()) {
      result = m2.group(0);
    }
    result = result.replace("(", "");
    result = result.replace(")", "");

    if (StringUtils.isEmpty(result)) {
      return null;
    }

    result = result.replace(" U;", "");
    result = result.replace(" zh-cn;", "");

    return result;
  }

  public static String getDeviceInfo1() {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    Browser browser = userAgent.getBrowser();
    Version version = browser.getVersion(request.getHeader("User-Agent"));

    System.out.println("获取浏览器信息: " + browser.getName());
    System.out.println("获取浏览器信息: " + version.getVersion());

    return userAgent.getOperatingSystem().getManufacturer().getName();
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
      HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
      String[] urls = uploadFileUrl.split("\\.");
      String name = uploadFileUrl.substring(uploadFileUrl.lastIndexOf("\\") + 1);
      name = name.substring(0, name.lastIndexOf("."));
      String suffix = urls[urls.length - 1];
      accessory_table_column = parentFileName + "\\" + name + "_" + new Date().getTime() + "." + suffix;
      String fileDirectory = request.getServletContext().getRealPath("UploadFiles");
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
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    HttpServletResponse response = requestAttributes.getResponse();
    response.reset(); // 非常重要 清空输出流
    InputStream is = null;
    OutputStream os = null;
    try {
      String fileDirectory = request.getServletContext().getRealPath("UploadFiles");
      String tableFileName = request.getParameter("fileUrl");
      tableFileName = new String(tableFileName.getBytes("iso-8859-1"), "utf-8");
      String fileUrl = fileDirectory + "\\" + file + "\\" + tableFileName;
      String uploadFileName = tableFileName.substring(0, tableFileName.indexOf("_")) + tableFileName.substring(tableFileName.lastIndexOf("."));

      // 设定输出文件头
      // response.setContentType("application/msexcel");// 定义输出类型
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

  /**
   * Excel 2007 下载
   * 
   * @param fileName
   * @param sql
   * @param params
   */
  public void downloadExcel(String fileName, Workbook workbook) {

    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    HttpServletResponse response = requestAttributes.getResponse();
    response.reset(); // 非常重要
    OutputStream os = null;
    try {
      fileName = fileName.trim();
      String userAgent = request.getHeader("user-agent");
      if (userAgent.indexOf("MSIE") >= 0) {
        URLCodec codec = new URLCodec();
        fileName = codec.encode(fileName, "UTF-8");
      } else {
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
      }

      // if (fileName.toLowerCase().indexOf(".htm") < 0) {
      // response.setHeader("Content-disposition", "attachment; filename=\"" +
      // fileName + "\"");
      // }

      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
      os = response.getOutputStream();
      workbook.write(os);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
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