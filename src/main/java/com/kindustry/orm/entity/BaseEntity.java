package com.kindustry.orm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author kindustry
 *
 */
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable, Cloneable {

  /**
   * 主键标识Id
   */
  @Id
  @Column(name = "sid")
  private String sid; //

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

  /**
   * sidを取得する。
   * 
   * @return the sid
   */
  public String getSid() {
    return sid;
  }

  /**
   * sidを設定する。
   * 
   * @param sid
   *          the sid to set
   */
  public void setSid(String sid) {
    this.sid = sid;
  }

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