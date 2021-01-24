package com.kindustry.system.service;

import java.util.List;
import java.util.Map;

import com.kindustry.system.entity.User;

/**
 * 
 * User 表数据服务层接口
 * 
 */
public interface IUserService /* extends IService<User> */{

  User selectByLoginName(String loginName);

  void deleteUser(Long userId);

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