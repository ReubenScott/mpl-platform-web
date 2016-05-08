package com.soak.attendance.model;

import java.util.Date;

import com.soak.framework.orm.Column;
import com.soak.framework.orm.Table;

@Table(name = "s_atnd_summary_sheet", pk = { "statdate", "empno" })
public class AtndSummarySheet {

  @Column(name = "statdate")
  private Date statdate; // 统计日期

  @Column(name = "empno")
  private String empno; // 员工号

  @Column(name = "empname")
  private String empname; // 员工姓名

  @Column(name = "deptname")
  private String deptname; // 所属部门
  
  @Column(name = "scheduleType")
  private String scheduleType; // 排班类型

  @Column(name = "atndstatus")
  private String atndStatus; // 考勤状态 1 正常 ； 2 迟到 ； 3 漏打卡

  @Column(name = "punch_in_time")
  private Date punchInTime; // time上班时间

  @Column(name = "punch_out_time")
  private Date punchOutTime; // time下班时间

  @Column(name = "business_triphours")
  private Float businessTrip; // float(4,2)出差

  @Column(name = "ordinary_overtime")
  private Float ordinaryOvertime; // float(4,2)平时加班

  @Column(name = "weekend_overtime")
  private Float weekendOvertime; // float(4,2)周末加班

  @Column(name = "holiday_overtime")
  private Float holidayOvertime; // float(4,2)法定加班

  @Column(name = "isLate")
  private Boolean isLate ; // 是否迟到 

  @Column(name = "meal_subsidy")
  private Boolean mealSubsidy; // 有无餐补

  @Column(name = "absence_hours")
  private Float absence; // float(4,2)请假时数

  @Column(name = "total_hours_worked")
  private Float totalHoursWorked; // float(4,2)总出勤

  @Column(name = "remark")
  private String remark; // varchar(20)备注

  @Column(name = "lastmth_accum_vacation")
  private Float lastMthAccumVacation; // float(4,2)上月存休

  @Column(name = "current_mth_accum_vacation")
  private Float currentMthAccumVacation; // float(4,2)本月存休

  private Float manualOvertime; // 手工填写的平时加班

  @Override
  public boolean equals(Object other) {
    if (other instanceof AtndSummarySheet) {
      AtndSummarySheet obj = (AtndSummarySheet) other;
      if ((this.statdate == null && this.statdate == obj.statdate) || (this.statdate != null && this.statdate.equals(obj.statdate))) {
        if ((this.empno == null && this.empno == obj.empno) || (this.empno != null && this.empno.equals(obj.empno))) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 37 * result + (this.statdate == null ? 0 : this.statdate.hashCode());
    result = 37 * result + (this.empno == null ? 0 : this.empno.hashCode());
    return result;
  }

  public Date getStatdate() {
    return statdate;
  }

  public void setStatdate(Date statdate) {
    this.statdate = statdate;
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

  public String getAtndStatus() {
    return atndStatus;
  }

  public void setAtndStatus(String atndStatus) {
    this.atndStatus = atndStatus;
  }

  public Date getPunchInTime() {
    return punchInTime;
  }

  public void setPunchInTime(Date punchInTime) {
    this.punchInTime = punchInTime;
  }

  public Date getPunchOutTime() {
    return punchOutTime;
  }

  public void setPunchOutTime(Date punchOutTime) {
    this.punchOutTime = punchOutTime;
  }

  public Float getBusinessTrip() {
    return businessTrip;
  }

  public void setBusinessTrip(Float businessTrip) {
    this.businessTrip = businessTrip;
  }

  public Float getOrdinaryOvertime() {
    return ordinaryOvertime;
  }

  public void setOrdinaryOvertime(Float ordinaryOvertime) {
    this.ordinaryOvertime = ordinaryOvertime;
  }

  public Float getWeekendOvertime() {
    return weekendOvertime;
  }

  public void setWeekendOvertime(Float weekendOvertime) {
    this.weekendOvertime = weekendOvertime;
  }

  public Float getHolidayOvertime() {
    return holidayOvertime;
  }

  public void setHolidayOvertime(Float holidayOvertime) {
    this.holidayOvertime = holidayOvertime;
  }


  public Float getAbsence() {
    return absence;
  }

  public void setAbsence(Float absence) {
    this.absence = absence;
  }

  public Float getTotalHoursWorked() {
    return totalHoursWorked;
  }

  public void setTotalHoursWorked(Float totalHoursWorked) {
    this.totalHoursWorked = totalHoursWorked;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Float getLastMthAccumVacation() {
    return lastMthAccumVacation;
  }

  public void setLastMthAccumVacation(Float lastMthAccumVacation) {
    this.lastMthAccumVacation = lastMthAccumVacation;
  }

  public Float getCurrentMthAccumVacation() {
    return currentMthAccumVacation;
  }

  public void setCurrentMthAccumVacation(Float currentMthAccumVacation) {
    this.currentMthAccumVacation = currentMthAccumVacation;
  }

  public Float getManualOvertime() {
    return manualOvertime;
  }

  public void setManualOvertime(Float manualOvertime) {
    this.manualOvertime = manualOvertime;
  }

  public Boolean getIsLate() {
    return isLate;
  }

  public void setIsLate(Boolean isLate) {
    this.isLate = isLate;
  }

  public Boolean getMealSubsidy() {
    return mealSubsidy;
  }

  public void setMealSubsidy(Boolean mealSubsidy) {
    this.mealSubsidy = mealSubsidy;
  }

  public String getScheduleType() {
    return scheduleType;
  }

  public void setScheduleType(String scheduleType) {
    this.scheduleType = scheduleType;
  }

  // get set **

}
