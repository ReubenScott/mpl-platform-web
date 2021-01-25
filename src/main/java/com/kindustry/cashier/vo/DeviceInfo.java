package com.kindustry.cashier.vo;

/**
 * 客户端
 */
public class DeviceInfo {

  // IP 地址
  private String ip;

  // 主机名
  private String host;

  // MAC地址
  private String mac;

  // 终端号
  private String terminalNo;

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getTerminalNo() {
    return terminalNo;
  }

  public void setTerminalNo(String terminalNo) {
    this.terminalNo = terminalNo;
  }

}