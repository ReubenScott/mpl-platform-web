package com.kindustry.framework.service.imp;


import javax.annotation.Resource;

import com.kindustry.framework.dao.IBaseDao;
import com.kindustry.framework.service.IBaseService;


public class BaseServiceImpl implements IBaseService {
 
 @Resource
 protected IBaseDao baseDao;

 public IBaseDao getBaseDao() {
  return baseDao;
 }

 public void setBaseDao(IBaseDao baseDao) {
  this.baseDao = baseDao;
 }
 
}