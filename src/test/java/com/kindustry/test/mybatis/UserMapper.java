package com.kindustry.test.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.kindustry.framework.orm.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper {
  public void insertUser(User user);

  public User getUser(String name);
}
