package com.kindustry.framework.config;

public class Configure {

  /**
   * 对保存实例的变量添加volatile的修饰
   */
  private volatile static Configure instance = null;

  private Configure() {
  }

  public static Configure getInstance() {
    // 先检查实例是否存在，如果不存在才进入下面的同步块
    if (instance == null) {
      // 同步块，线程安全的创建实例
      synchronized (Configure.class) {
        // 再次检查实例是否存在，如果不存在才真的创建实例
        if (instance == null) {
          instance = new Configure();
        }
      }
    }
    return instance;
  }

  // TODO read app config
  public String getValue(String key) {
    // TODO Auto-generated method stub

    return null;
  }

}
