package com.kindustry.framework.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author kindustry
 *
 */
public class BaseController {

  @Resource
  protected HttpServletRequest request;

  @Resource
  protected HttpServletResponse response;

  public Map<String, String> getHeaderMap() {
    Enumeration<String> headerNames = request.getHeaderNames();
    Map<String, String> headerMap = new HashMap<String, String>();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
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
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST,GET");
    response.setHeader("Access-Control-Allow-Headers:x-requested-with", "content-type");
  }

}