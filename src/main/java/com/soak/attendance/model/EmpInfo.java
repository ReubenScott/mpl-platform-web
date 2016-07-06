package com.soak.attendance.model;

import com.soak.framework.orm.Column;
import com.soak.framework.orm.Table;

/**
 * 
 * 排班类型
 */
@Table(name = "f_emp_info", pk = { "empNO" })
public class EmpInfo {

  @Column(name = "empNO")
  private String empNO; // 编号

  @Column(name = "empNAME")
  private String empName; // 姓名

  @Column(name = "deptid")
  private String deptId; // 部门编号

  private String deptName; // 部门编号
  
  
  public EmpInfo(){
    
  }

  public EmpInfo(String empNO){
    this.empNO = empNO ;
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