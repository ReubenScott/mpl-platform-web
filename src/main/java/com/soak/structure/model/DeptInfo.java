package com.soak.structure.model;

import com.soak.framework.orm.Column;
import com.soak.framework.orm.Table;

/**
 * 
 * 排班类型
 */
@Table(name = "f_dept_info", pk = {"uid" })
public class DeptInfo {

  @Column(name = "uid")
  private String sid; // 部门编号
  
  @Column(name = "DeptNO")
  private String deptNO; // 编号

  @Column(name = "DeptNAME")
  private String deptName; // 姓名

  private String status; // 人员状态

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public String getDeptNO() {
    return deptNO;
  }

  public void setDeptNO(String deptNO) {
    this.deptNO = deptNO;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  
  

}