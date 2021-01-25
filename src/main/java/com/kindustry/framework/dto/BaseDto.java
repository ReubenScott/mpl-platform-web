package com.kindustry.framework.dto;

import java.io.Serializable;

/**
 * Author: D.Yang Email: koyangslash@gmail.com Date: 16/8/31 Time: 下午5:50
 * Describe: 封装Json返回信息
 */
public class BaseDto implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6877561452672002978L;

  /** 是否成功 */
  private boolean success;

  /** 消息码 */
  private String status;

  /** 消息 */
  private String msg;

  /** 数据 */
  private Object data;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

}
