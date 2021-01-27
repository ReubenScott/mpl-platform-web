package com.kindustry.cashier.constant;

/**
 * 
 * 考勤状态
 */
public enum DeviceType {

  Normal(1, "Normal") { // 正常
    @Override
    public int maxActive() {
      return 1;
    }
  },
  Business(2, "Business") {
    @Override
    public int maxActive() {
      return 5;
    }
  };

  // 成员变量
  private final int type;
  private final String title;

  // 最大客戶數
  abstract int maxActive();

  // 查詢
  public DeviceType getType(String title) {
    for (DeviceType type : values()) {
      if (type.getTitle().equals(title)) {
        return type;
      }
    }
    return null;
  }

  // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
  DeviceType(int type, String title) {
    this.type = type;
    this.title = title;
  }

  public int getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

}