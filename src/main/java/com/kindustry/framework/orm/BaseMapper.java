package com.kindustry.framework.orm;

/**
 * 数据库访问标识接口
 *
 * @author TonyJ
 * @time 2015-1-31 下午03:45:52
 * @email tanglongjia@126.com
 */
// @MapperScan
public interface BaseMapper<T extends BaseEntity> {

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
  // @SelectProvider(type = BaseSqlProvider.class, method = "get")
  T selectSingle(Object param);

  /**
   * 保存
   * 
   * @param entity
   * @return
   */
  // @InsertProvider(type = BaseSqlProvider.class, method = "insert")
  T insert(T entity);

  /**
   * 更新
   * 
   * @param entity
   * @return
   */
  // @UpdateProvider(type = BaseSqlProvider.class, method = "update")
  boolean update(T entity);

  /**
   * 删除一个
   * 
   * @param param
   * @return
   */
  // @DeleteProvider(type = BaseSqlProvider.class, method = "delete")
  boolean delete(Object param);

  // @SelectProvider(type = BaseSqlProvider.class, method = "isExisted")
  // boolean isExisted(PK pk);

}