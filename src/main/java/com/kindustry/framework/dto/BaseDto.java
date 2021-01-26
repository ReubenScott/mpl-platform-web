package com.kindustry.framework.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * @author chenjun Describe: 封装Json返回信息
 */
@JsonPropertyOrder({ "success", "state", "msg", "data" })
public class BaseDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 是否成功 */
  @JsonProperty("out")
  private boolean success;

  /** 消息码 */
  private Integer state;

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

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
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