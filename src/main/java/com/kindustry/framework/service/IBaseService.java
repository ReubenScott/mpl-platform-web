package com.kindustry.framework.service;

import com.kindustry.framework.dao.IBaseDao;

public interface IBaseService {

  public IBaseDao getBaseDao();

  public void setBaseDao(IBaseDao baseDao);
  

}