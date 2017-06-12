package com.soak.attendance.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * 打卡记录
 * 
 */
@Table(name = "f_atnd_punch_record")
public class PunchRecord {

  @Id
  @Column(name = "uid")
  private String uid; // 

  @Column(name = "SEQNO")
  private String seqno; // 

  @Column(name = "card_ID")
  private String cardID; // 

  @Column(name = "empno")
  private String empno; // 

  @Column(name = "empname")
  private String empname; // 

  @Column(name = "deptname")
  private String deptname; // varchar(10)

  @Column(name = "recordTime")
  private Timestamp punchTime; // 打卡时间

  @Column(name = "location")
  private String location; // 开始时间

  private Boolean state; // tinyint(1)0:通过,1:不通过;

  @Column(name = "remark")
  private String remark; // 描述

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getSeqno() {
    return seqno;
  }

  public void setSeqno(String seqno) {
    this.seqno = seqno;
  }

  public String getCardID() {
    return cardID;
  }

  public void setCardID(String cardID) {
    this.cardID = cardID;
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

  public Timestamp getPunchTime() {
    return punchTime;
  }

  public void setPunchTime(Date punchTime) {
    this.punchTime = new java.sql.Timestamp(punchTime.getTime()); 
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Boolean getState() {
    return state;
  }

  public void setState(Boolean state) {
    this.state = state;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
