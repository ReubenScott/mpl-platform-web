package com.kindustry.orm.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.kindustry.orm.jdbc.SqlProvider;

/**
 * 数据库访问标识接口
 * 
 * @author kindustry
 *
 * @param <T>
 */
public interface BaseMapper<T> {

  /**
   * 保存
   * 
   * @param entity
   * @return
   */
  @InsertProvider(type = SqlProvider.class, method = "insert")
  boolean save(T entity);

  /**
   * 更新
   * 
   * @param entity
   * @return
   */
  @UpdateProvider(type = SqlProvider.class, method = "update")
  int update(T entity, Set<String> excludeColumns);

  /**
   * 按主鍵删除记录
   * 
   * @param entity
   * @return
   * @author kindustry
   */
  @DeleteProvider(type = SqlProvider.class, method = "deleteByPk")
  int deleteByPk(T entity);

  /**
   * 根據模板删除记录
   * 
   * @param entity
   * @return
   * @author kindustry
   */
  @DeleteProvider(type = SqlProvider.class, method = "deleteByExample")
  int deleteByExample(T entity, String... includeColumns);

  /**
   * 
   * @param pk
   * @return
   * @author kindustry
   */
  @SelectProvider(type = SqlProvider.class, method = "isExisted")
  String isExisted(T pk);

  /**
   * 
   * @param pk
   * @return
   * @author kindustry
   */
  @SelectProvider(type = SqlProvider.class, method = "get")
  T get(T pk);

  /**
   * 查询单个
   * 
   * @param param
   * @return
   */
  T selectSingle(T param);

  /**
   * 
   * @param entityClass
   * @param id
   * @return
   * @author kindustry
   */
  T findById(Class<T> entityClass, int id);

  /**
   * 
   * @param entityClass
   * @return
   * @author kindustry
   */
  List<T> findAll(Class<T> entityClass);

  /**
   * 
   * 返回查询一览表的信息
   * 
   * @param <T>
   * @param _mybitsId
   *          mybatis中对应业务标识
   * @param _params
   * @return
   */
  public <T> List<T> query(String _mybitsId, Map<String, Object> _params);

  /**
   * 查询相关列表信息
   * 
   * @param <T>
   *          返回数据
   * @param id
   *          mybatis中对应业务标识
   * @param _params
   * @return
   */
  public <T> List<T> query(String _mybitsId, Object _params);

  /**
   * 查询单个数据
   * 
   * @param queryString
   * @param object
   * @return
   */
  public Object queryOne(String queryString, Object object);

  /**
   * 单表插入记录
   * 
   * @param obj
   */
  public <T> int insert(String _mybitsId, T obj);

  /**
   * 更新单表
   * 
   * @param obj
   */
  public <T> int update(String _mybitsId, T obj);

  /**
   * 删除记录
   * 
   * @param clz
   * @param id
   */
  public <T> int delete(String _mybitsId, T obj);

}