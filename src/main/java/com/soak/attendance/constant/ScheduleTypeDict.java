package com.soak.attendance.constant;

/**
 * 
 * 排班类型状态
 */
public enum ScheduleTypeDict {
  
  DAYSHIFT { // 默认白班
    @Override
    public String getName() {
      return "白班";
    }

    @Override
    public String getValue() {
      return "D";
    }
  }, 
  NIGHTSHIFT1 { 
    @Override
    public String getName() {
      return "夜班1";
    }

    @Override
    public String getValue() {
      return "N1";
    }
  },
  NIGHTSHIFT2 {
    @Override
    public String getName() {
      return "夜班2";
    }

    @Override
    public String getValue() {
      return "N2";
    }
  }
  ;

  public abstract String getName();

  public abstract String getValue();



  // take a day off (请一天假)
  // ask for leave （请假）
  // grant leave （准假）
  // sick leave （病假）
  // maternity leave（产假）
  // annual leave（年假）
  // on leave告假中
  // take French leave不告而别
  // paid leave有薪假
  // 例; I get leave twice a year. = I have two leaves a year. (我一年有两次休假)

}