package com.soak.attendance.constant;

import java.util.Date;
import java.util.Map;

import com.soak.common.date.DateUtil;
import com.soak.common.util.StringUtil;
import com.soak.framework.jdbc.core.JdbcTemplate;


/**
 * 
 * 排班类型状态
 */
public enum ScheduleTypeDict {
  
//  DAYSHIFT      ( "白班",  "D" , "7:30" , "17:00"  ){
//    public String getRestPeriodStart(){
//      return "11:30";
//    }
//    
//    public String getRestPeriodEnd(){
//      return "14:00";
//    }
//  },  //默认白班
//  NIGHTSHIFT1   ( "夜班1", "N1", "17:00", "01:00"  ), // 白班后接的夜班
//  NIGHTSHIFT2   ( "夜班2", "N2", "19:00", "03:00"  ),
//  NIGHTSHIFT3   ( "夜班3", "N3", "19:30", "03:30"  ),
//  MORNINGSHIFT1 ( "早班1", "M1", "7:00" , "15:00"  ), 
//  MORNINGSHIFT2 ( "早班2", "M2", "7:30" , "15:30"  ), 
//  SWINGSHIFT1   ( "中班1", "S1", "15:00", "23:00"  ), 
//  SWINGSHIFT2   ( "中班2", "S2", "15:30", "23:30"  ), 
//  EVENINGSHIFT1 ( "晚班1", "E1", "23:00", "7:00"   ), 
//  EVENINGSHIFT2 ( "晚班2", "E2", "23:30", "7:30"   ), 
//  ;
  
  DAYSHIFT      ( "白班",  "D"   ){
    public String getRestPeriodStart(){
      return "11:30";
    }
    
    public String getRestPeriodEnd(){
      return "13:00";
    }
  },  //默认白班
  NIGHTSHIFT1   ( "夜班1", "N1" ), 
  NIGHTSHIFT2   ( "夜班2", "N2" ),
  NIGHTSHIFT3   ( "夜班3", "N3" ),
  MORNINGSHIFT1 ( "早班1", "M1" ), 
  MORNINGSHIFT2 ( "早班2", "M2" ), 
  SWINGSHIFT1   ( "中班1", "S1" ), 
  SWINGSHIFT2   ( "中班2", "S2" ), 
  EVENINGSHIFT1 ( "晚班1", "E1" ), 
  EVENINGSHIFT2 ( "晚班2", "E2" ), 
  ;
   
  // 成员变量
  private String name;   
  private String value;
  private String startTime;
  private String endTime;
  private String restPeriodStart;
  private String restPeriodEnd;
  

  // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
  private ScheduleTypeDict(String name, String value , String startTime, String endTime) {
    this.name = name;
    this.value = value;
    this.startTime = startTime;
    this.endTime = endTime;
  }
  
  // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
  private ScheduleTypeDict(String name, String value ) {
    this.name = name;
    this.value = value;
    JdbcTemplate jdbcHandler = JdbcTemplate.getInstance();
//    String sql = "SELECT MIN(starttime) AS starttime , MAX(endtime) AS endtime FROM dim_scheduletype WHERE scheduleCODE = ? AND ScheduleName = ? " ;
    
    StringBuffer sql = new StringBuffer("SELECT scheduleCODE , ScheduleName , MIN(starttime) AS starttime , MAX(endtime) AS endtime ");
    sql.append(", CASE WHEN COUNT(1)=2 THEN MIN(endtime) END AS restPeriodStart ");
    sql.append(", CASE WHEN COUNT(1)=2 THEN MAX(starttime) END  AS restPeriodEnd ");
    sql.append(" FROM dim_scheduletype ");
    sql.append(" WHERE scheduleCODE = ? AND ScheduleName = ?  ");    
    sql.append(" GROUP BY  scheduleCODE , ScheduleName");
    
    Map rs =  jdbcHandler.queryOneAsMap(sql.toString(), new String[]{ value , name });
    this.startTime = rs.get("starttime").toString();
    this.endTime = rs.get("endtime").toString();
    this.restPeriodStart = rs.get("restPeriodStart") == null ? null : rs.get("restPeriodStart").toString() ;
    this.restPeriodEnd = rs.get("restPeriodEnd") == null ? null : rs.get("restPeriodEnd").toString() ;
  }

  //  休息时间
  public String getRestPeriodStart(){
    return restPeriodStart;
  }

  public String getRestPeriodEnd(){
    return restPeriodEnd;
  }
  
  // get set 方法
  public String getName() {
    return name;
  }
  
  public String getValue() {
    return value;
  }
  
  public String getStartTime() {
//    SELECT MIN(starttime) FROM dim_scheduletype WHERE scheduleCODE = 'D' 
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public Date getStartTime(Date dateStr) {
    return DateUtil.setTime(dateStr, startTime);
  }

  public Date getStartTime(String dateStr) {
    return DateUtil.setTime(dateStr, startTime);
  }

  public Date getEndTime(String dateStr) {
    Date start = getStartTime(dateStr);
    Date end = DateUtil.setTime(dateStr, endTime);
    if(start.after(end)){
      end = DateUtil.addDays(end, 1);
    }
    return end;
  }

  public Date getEndTime(Date dateStr) {
    Date start = getStartTime(dateStr);
    Date end = DateUtil.setTime(dateStr, endTime);
    if(start.after(end)){
      end = DateUtil.addDays(end, 1);
    }
    return end;
  }


  public Date getRestPeriodStart(Date dateStr){
    String per  = getRestPeriodStart();
    if(StringUtil.isEmpty(per)){
      return null;
    }
    return DateUtil.setTime(dateStr, per);
  }

  public Date getRestPeriodEnd(Date dateStr){
    String perend  = getRestPeriodEnd();
    if(StringUtil.isEmpty(perend)){
      return null;
    }
    return DateUtil.setTime(dateStr, perend);
  }
  
  
}