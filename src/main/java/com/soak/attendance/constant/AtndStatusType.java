package com.soak.attendance.constant;

/**
 * 
 * 考勤状态
 */
public enum AtndStatusType {

  Normal { // 正常
    @Override
    public String getName() {
      return "正常";
    }

    @Override
    public String getValue() {
      return "00";
    }
  }, 
  BusinessTrip { 
    @Override
    public String getName() {
      return "出差";
    }

    @Override
    public String getValue() {
      return "01";
    }
  },
  ORDINARY_OVERTIME {
    @Override
    public String getName() {
      return "平时加班";
    }

    @Override
    public String getValue() {
      return "10";
    }
  },
  WEEKEND_OVERTIME { 
    @Override
    public String getName() {
      return "周末加班";
    }

    @Override
    public String getValue() {
      return "11";
    }
  },
  HOLIDAY_OVERTIME { 
    @Override
    public String getName() {
      return "法定加班";
    }

    @Override
    public String getValue() {
      return "12";
    }
  },
  Late { 
    @Override
    public String getName() {
      return "迟到";
    }

    @Override
    public String getValue() {
      return "21";
    }
  },
  MissedPunch { // 漏打卡
    @Override
    public String getName() {
      return "漏打卡";
    }

    @Override
    public String getValue() {
      return "22";
    }
  },
  Absence { // 请假
    @Override
    public String getName() {
      return "请假";
    }

    @Override
    public String getValue() {
      return "20";
    }
  },
  Absenteeism { // 旷工
    @Override
    public String getName() {
      return "旷工";
    }

    @Override
    public String getValue() {
      return "30";
    }
  };

  public abstract String getName();

  public abstract String getValue();


  public static AtndStatusType getType(String statusType) {
    for (AtndStatusType ctype : values()) {
      if (ctype.getValue().equals(statusType)) {
        return ctype;
      }
    }

    return null;
  }

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