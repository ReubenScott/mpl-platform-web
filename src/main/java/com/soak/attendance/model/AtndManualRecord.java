package com.soak.attendance.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.soak.attendance.constant.ScheduleTypeDict;
import com.soak.common.date.DateUtil;

/***
 * 
 * 加班、外出、请假 手工记录 表
 * 
 * 1:加班,2:出差,3:请假;
 * 
 * 
 */
@Table(name = "atnd_manual_record")
public class AtndManualRecord {

  public static final int OVERTIME = 1;
  public static final int BUSINESSTRIP = 2;
  public static final int OFFWORK = 3;

  @Id
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

  @Column(name = "vacationType")
  private String vacationType; // 休假类型

  @Column(name = "starttime")
  private Timestamp startTime; // DATETIME COMMENT '开始时间' ,

  @Column(name = "endtime")
  private Timestamp endTime; // COMMENT '结束时间' ,

  @Column(name = "totalhours")
  private Float totalHours; // COMMENT '小时数' ,

  @Column(name = "isexempt")
  private Boolean isExempt; // 是不是 免检

  private String remark;

  @Column(name = "src_dt")
  private Date srcDate;

  @Column(name = "etl_dt")
  private Date etlDate;

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

  public Timestamp getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = new java.sql.Timestamp(startTime.getTime()); 
  }

  public Timestamp getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = new java.sql.Timestamp(endTime.getTime()); 
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

  public Date getSrcDate() {
    return srcDate;
  }

  public void setSrcDate(Date srcDate) {
    this.srcDate = srcDate;
  }

  public Date getEtlDate() {
    return etlDate;
  }

  public void setEtlDate(Date etlDate) {
    this.etlDate = etlDate;
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

  public String getVacationType() {
    return vacationType;
  }

  public void setVacationType(String vacationType) {
    this.vacationType = vacationType;
  }

}
