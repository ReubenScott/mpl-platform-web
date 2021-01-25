package com.kindustry.framework.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author kindustry
 *
 */
public class BaseController {

  @Autowired
  protected HttpServletRequest request;

  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    CustomDateEditor dateEditor = new CustomDateEditor(format, true);
    binder.registerCustomEditor(Date.class, dateEditor);
  }

  public <T> T getAttribute(String attributeName) {
    return (T) this.getRequest().getAttribute(attributeName);
  }

  public void setAttribute(String attributeName, Object object) {
    this.getRequest().setAttribute(attributeName, object);
  }

  public Object getSession(String attributeName) {
    return this.getRequest().getSession(true).getAttribute(attributeName);
  }

  public void setSession(String attributeName, Object object) {
    this.getRequest().getSession(true).setAttribute(attributeName, object);
  }

  public HttpServletRequest getRequest() {
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    return ((ServletRequestAttributes) ra).getRequest();
  }

  public HttpServletResponse getResponse() {
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    // return ((ServletRequestAttributes) ra).getResponse();
    return null;
  }

  public HttpSession getSession() {
    return this.getRequest().getSession(true);
  }

  public String getParameter(String paraName) {
    return this.getRequest().getParameter(paraName);
  }

  /**
   * 获取表单格式数据(或url拼接参数)
   * 
   * @return
   */
  public Map<String, String[]> getParameterMap() {
    return this.getRequest().getParameterMap();
  }

  public String getHeader(String headerName) {
    return this.getRequest().getHeader(headerName);
  }

  public Map getHeaderMap() {
    Enumeration headerNames = this.getRequest().getHeaderNames();
    Map headerMap = new HashMap();
    while (headerNames.hasMoreElements()) {
      String headerName = (String) headerNames.nextElement();
      String headerValue = getRequest().getHeader(headerName);
      headerMap.put(headerName, headerValue);
    }
    return headerMap;
  }

  /**
   * 获取客户端IP地址
   * 
   * @return
   */
  public String getClientIpAddress() {
    HttpServletRequest request = this.getRequest();
    // String ip = request.getHeader("x-forwarded-for");
    // if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    // ip = request.getHeader("Proxy-Client-IP");
    // }
    // if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    // ip = request.getHeader("WL-Proxy-Client-IP");
    // }
    // if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    // ip = request.getRemoteAddr();
    // }
    // if (ip.contains(",")) {
    // return ip.split(",")[0];
    // } else {
    // return ip;
    // }
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null) {
      if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
        int index = ip.indexOf(",");
        if (index != -1) {
          return ip.substring(0, index);
        } else {
          return ip;
        }
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (ip != null) {
      if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
        return ip;
      }
    }
    ip = request.getHeader("Proxy-Client-IP");
    if (ip != null) {
      if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
        return ip;
      }
    }
    ip = request.getHeader("WL-Proxy-Client-IP");
    if (ip != null) {
      if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
        return ip;
      }
    }
    ip = request.getRemoteAddr();
    return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
  }

  /**
   * 获取服务器ip地址
   * 
   * @return
   */
  public String getServerIpAddress() {
    InetAddress address;
    String serverIpAddress = null;
    try {
      address = InetAddress.getLocalHost(); // 获取的是本地的IP地址
      // //PC-20140317PXKX/192.168.0.121
      serverIpAddress = address.getHostAddress();// 192.168.0.121
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return serverIpAddress;
  }

  /**
   * 获取json格式数据
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  // public Map<String, Object> getRequestMap() {
  // try {
  // // 默认为json
  // InputStream inStream = this.getRequest().getInputStream();
  // BufferedReader in = new BufferedReader(new InputStreamReader(inStream,
  // "UTF-8"));
  // StringBuffer stringBuffer = new StringBuffer();
  // String buffer = "";
  // while (null != (buffer = (in.readLine()))) {
  // stringBuffer.append(buffer);
  // }
  // String reqDoc = stringBuffer.toString();
  // if (reqDoc == null || reqDoc.equals("")) {
  // return null;
  // }
  // return JsonUtil.toMap(reqDoc);
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // return null;
  // }
  /**
   * 允许跨域访问
   */
  public void allowCrossDomainAccess() {
    HttpServletResponse servletResponse = getResponse();
    servletResponse.setHeader("Access-Control-Allow-Origin", "*");
    servletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET");
    servletResponse.setHeader("Access-Control-Allow-Headers:x-requested-with", "content-type");
  }

}