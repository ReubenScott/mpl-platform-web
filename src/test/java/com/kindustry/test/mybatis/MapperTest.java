package com.kindustry.test.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

public class MapperTest {

  static SqlSessionFactory sqlSessionFactory = null;

  static {
    sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
  }

  // @Test
  public void testAdd() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      UserMapper mapper = sqlSession.getMapper(UserMapper.class);
      User user = new User("lisi", new Integer(25));
      mapper.insertUser(user);
      sqlSession.commit();// 这里一定要提交，不然数据进不去数据库中
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void getUser() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      UserMapper mapper = sqlSession.getMapper(UserMapper.class);
      User user = mapper.getUser("lisi");
      System.out.println("name: " + user.getName() + "|age: " + user.getAge());

      System.out.println(user);
    } finally {
      sqlSession.close();
    }
  }
}
