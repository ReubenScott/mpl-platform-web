package com.soak.attendance.model;

import java.util.Date;

import com.soak.framework.orm.Column;
import com.soak.framework.orm.Table;

/***
 * 
 * 请假单
 * 
 * 
 */
@Table(name = "atnd_offwork_record", pk = "uid")
public class OffWorkRecord {

  @Column(name = "uid")
  private String uid;

  @Column(name = "empno")
  private String empno; // 员工号

  @Column(name = "empname")
  private String empname;// '员工姓名',

  @Column(name = "deptno")
  private String deptno; // 部门编号

  @Column(name = "deptname")
  private String deptname; // 所属部门,

  @Column(name = "vacationType")
  private String vacationType; // 休假类型

  @Column(name = "starttime")
  private Date startTime; // DATETIME COMMENT '开始时间' ,

  @Column(name = "endtime")
  private Date endTime; // COMMENT '结束时间' ,

  @Column(name = "total_hours")
  private float totalHours; // COMMENT '小时数' ,

  private String remark;

  @Column(name = "src_dt")
  private Date srcDt;

  private Date etl_dt;

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

  public String getDeptno() {
    return deptno;
  }

  public void setDeptno(String deptno) {
    this.deptno = deptno;
  }

  public String getDeptname() {
    return deptname;
  }

  public void setDeptname(String deptname) {
    this.deptname = deptname;
  }

  public String getVacationType() {
    return vacationType;
  }

  public void setVacationType(String vacationType) {
    this.vacationType = vacationType;
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

  public float getTotalHours() {
    return totalHours;
  }

  public void setTotalHours(float totalHours) {
    this.totalHours = totalHours;
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

}
