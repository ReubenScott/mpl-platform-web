package com.soak.attendance.bean;

import java.util.Date;


public class PunchRecord {
  
  private Date clockTime ;   // 打卡时间
  
  private Date startTime ;   // 开始时间

  private Date endTime ;   // 结束时间
  
  private Integer type ; // 打卡类型
  
  private String desc ;  // 描述

  public Date getClockTime() {
    return clockTime;
  }

  public void setClockTime(Date clockTime) {
    this.clockTime = clockTime;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  
  
  
}



