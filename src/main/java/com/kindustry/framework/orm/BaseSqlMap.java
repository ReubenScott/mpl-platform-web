package com.kindustry.framework.orm;

/**
 *@author TonyJ
 *@time 2015-1-31 下午03:45:52
 *@email tanglongjia@126.com
 */
public interface BaseSqlMap<T extends BaseEntity> extends SqlMap {

  /**
   * 保存
   */
  public static final String SQL_INSERT = ".insert";

  /**
   * 删除
   */
  public static final String SQL_DELETE = ".delete";

  /**
   * 更新
   */
  public static final String SQL_UPDATE = ".update";

  /**
   * 根据Id查询
   */
  public static final String SQL_SELECT_SINGLE = ".selectSingle";

  /**
   * 查询单个
   * 
   * @param param
   * @return
   */
  T selectSingle(Object param);

  /**
   * 保存
   * 
   * @param entity
   * @return
   */
  T insert(T entity);

  /**
   * 更新
   * 
   * @param entity
   * @return
   */
  boolean update(T entity);

  /**
   * 删除一个
   * 
   * @param param
   * @return
   */
  boolean delete(Object param);

}