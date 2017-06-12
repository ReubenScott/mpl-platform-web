package com.soak.structure.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 排班类型
 */
@Table(name = "f_emp_info")
public class EmpInfo {

  @Id
  @Column(name = "empNO")
  private String empNO; // 编号

  @Column(name = "empNAME")
  private String empName; // 姓名

  @Column(name = "deptid")
  private String deptId; // 部门编号

  private String deptName; // 部门编号

  private String status; // 人员状态

  private String entryate; // 入职日期

  public EmpInfo() {

  }

  public EmpInfo(String empNO) {
    this.empNO = empNO;
  }

  public String getEmpNO() {
    return empNO;
  }

  public void setEmpNO(String empNO) {
    this.empNO = empNO;
  }

  public String getEmpName() {
    return empName;
  }

  public void setEmpName(String empName) {
    this.empName = empName;
  }

  public String getDeptId() {
    return deptId;
  }

  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

}