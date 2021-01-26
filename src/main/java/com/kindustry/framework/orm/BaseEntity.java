package com.kindustry.framework.orm;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author kindustry
 *
 */
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable, Cloneable {

  /**
   * 标识Id
   */
  // private long id;

  /**
   * 用户名
   */
  @JsonIgnore
  private String createUser;

  /**
   * 创建时间
   */
  @JsonIgnore
  private Date createTime;

  /**
   * 更新时间
   */
  @JsonIgnore
  private Date updateTime;

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}