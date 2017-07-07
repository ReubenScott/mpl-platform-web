package com.kindustry.attendance.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 排班类型
 */
@Table(name = "dim_ScheduleType")
public class ScheduleType {

  
  @Id
  @Column(name = "schedulecode")
  private String scheduleid; // 排班类型

  @Column(name = "schedulename")
  private String scheduleName; // 排班类型名称

  @Column(name = "starttime")
  private Date starttime; // 开始时间

  @Column(name = "hours")
  private Float hours; // 排班小时

  
  public String getScheduleid() {
    return scheduleid;
  }

  public void setScheduleid(String scheduleid) {
    this.scheduleid = scheduleid;
  }

  public String getScheduleName() {
    return scheduleName;
  }

  public void setScheduleName(String scheduleName) {
    this.scheduleName = scheduleName;
  }

  public Date getStarttime() {
    return starttime;
  }

  public void setStarttime(Date starttime) {
    this.starttime = starttime;
  }

  public Float getHours() {
    return hours;
  }

  public void setHours(Float hours) {
    this.hours = hours;
  }

}