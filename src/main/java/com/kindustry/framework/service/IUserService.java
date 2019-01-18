package com.kindustry.framework.service;

import java.util.List;
import java.util.Map;


public interface IUserService extends IBaseService {

  /**
   * 获取单条记录
   */
  public Map queryUserById(Map map);

  /**
   * 获取多条记录
   */
  public List<Map> queryUsers(Map map);

  /**
   * 插入记录
   * 
   * @throws Exception
   */
  public void insertUser(Map map) throws Exception;

  /**
   * 删除记录
   * 
   * @throws Exception
   */
  public void deleteUser(Map map) throws Exception;

  /**
   * 更新记录
   */
  public void updateUser(Map map);

}
