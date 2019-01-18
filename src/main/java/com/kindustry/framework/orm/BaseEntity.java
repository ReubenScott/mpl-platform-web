package com.kindustry.framework.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *@author TonyJ
 *@time 2015-1-31 下午03:37:46
 *@email tanglongjia@126.com
 */
public class BaseEntity implements Serializable, Cloneable {

  /** 
         *  
         */
  private static final long serialVersionUID = 2413960745071474764L;

  /**
   *标识Id
   */
  private long id;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 更新时间
   */
  private Date updateTime;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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