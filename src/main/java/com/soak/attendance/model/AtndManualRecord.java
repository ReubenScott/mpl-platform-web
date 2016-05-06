package com.soak.attendance.model;

import java.util.Date;

import com.soak.attendance.constant.ScheduleTypeDict;
import com.soak.framework.orm.Column;
import com.soak.framework.orm.Table;

/***
 * 
 * 加班、外出、请假 手工记录 表
 * 
 * 1:加班,2:出差,3:请假;
 * 
 * @author reuben
 * 
 */
@Table(name = "atnd_manual_record", pk = "uid")
public class AtndManualRecord {

  @Column(name = "uid")
  private String uid;

  private String deptno; // 部门编号

  @Column(name = "deptname")
  private String deptname; // 所属部门,

  @Column(name = "empno")
  private String empno; // 员工号

  @Column(name = "empname")
  private String empname;// '员工姓名',

  @Column(name = "booktype")
  private Integer bookType; // '1:加班,2:出差,3:请假;' ,

  @Column(name = "scheduleType")
  private String scheduleType; // 排班类型

  @Column(name = "starttime")
  private Date startTime; // DATETIME COMMENT '开始时间' ,

  @Column(name = "endtime")
  private Date endTime; // COMMENT '结束时间' ,

  @Column(name = "totalhours")
  private Float totalHours; // COMMENT '小时数' ,

  @Column(name = "isexempt")
  private Boolean isExempt; // 是不是 免检

  private String remark;

  @Column(name = "SRC_DT")
  private Date srcDt;

  private Date etl_dt;

  /***
   * 通过加班单 获取 某天 排班类型
   * 
   * @param overtimeRecords
   *          某一员工的加班单
   */
  public ScheduleTypeDict getCurrentScheduleType() {
    for (ScheduleTypeDict schedule : ScheduleTypeDict.values()) {
      if (schedule.getValue().equals(scheduleType)) {
        return schedule;
      }
    }
    return null;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getEmpno() {
    return empno;
  }

  public void setEmpno(String empno) {
    this.empno = empno;
  }

  public String getEmpname() {
    return empname;
  }

  public void setEmpname(String empname) {
    this.empname = empname;
  }

  public String getDeptname() {
    return deptname;
  }

  public void setDeptname(String deptname) {
    this.deptname = deptname;
  }

  public Integer getBookType() {
    return bookType;
  }

  public void setBookType(Integer bookType) {
    this.bookType = bookType;
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

  public Float getTotalHours() {
    return totalHours;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getSrcDt() {
    return srcDt;
  }

  public void setSrcDt(Date srcDt) {
    this.srcDt = srcDt;
  }

  public Date getEtl_dt() {
    return etl_dt;
  }

  public void setEtl_dt(Date etlDt) {
    etl_dt = etlDt;
  }

  public String getDeptno() {
    return deptno;
  }

  public void setDeptno(String deptno) {
    this.deptno = deptno;
  }

  public String getScheduleType() {
    return scheduleType;
  }

  public void setScheduleType(String scheduleType) {
    this.scheduleType = scheduleType;
  }

  public void setTotalHours(Float totalHours) {
    this.totalHours = totalHours;
  }

  public Boolean getIsExempt() {
    return isExempt;
  }

  public void setIsExempt(Boolean isExempt) {
    this.isExempt = isExempt;
  }

}
