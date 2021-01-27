package com.kindustry.cashier.constant;

public enum UserType {
  MEMBER(0, "普通用户"), ADMIN(1, "管理员");

  private final int key;
  private final String desc;

  UserType(final int key, final String desc) {
    this.key = key;
    this.desc = desc;
  }

  public int key() {
    return this.key;
  }

  public String desc() {
    return this.desc;
  }

}
