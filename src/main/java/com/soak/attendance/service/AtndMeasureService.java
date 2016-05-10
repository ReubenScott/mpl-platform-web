package com.soak.attendance.service;import java.util.Date;import java.util.List;import com.soak.attendance.constant.DateTypeDict;import com.soak.attendance.model.EmpInfo;import com.soak.attendance.model.ScheduleType;public interface AtndMeasureService {    /**   *    * 检测 当前日期   工作类型   * @param date   * @param empName   *    */  public DateTypeDict checkOneDayTypeByEmpId(Date date, String empid);    /**   *    * 检测 当前日期   工作类型   * @param date   * @param empName   *    */  public DateTypeDict checkOneDayTypeByEmpName(Date date, String empName);    /**   * 获取所有排班类型   */  public List<ScheduleType> getAllScheduletypes();    /**   * 第一步 导入打卡记录   *    * @param filePath   */  public void loadPunchRecord(String dirPath);    /**   * 加班单 录入   *    * @param filePath   */  public void loadOvertimeWorkApplicationForm(String filePath) ;      /**   * 出差单 录入   *    * @param filePath   */  public void loadBusinessTripApplicationForm(String filePath) ;    /**   * 请假单 录入   *    * @param filePath   */  public void loadOffWorkApplicationForm(String filePath) ;      /**   * 考情统计   * @param emp   * @param yyyyMM   */  public void atndmeasureTest(String yyyyMM, String...  empnos) ;    }