package com.kindustry.orm.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BaseMapperBridge<T, M extends BaseMapper<T>> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 保存
   */
  final String SQL_INSERT = ".insert";

  /**
   * 删除
   */
  final String SQL_DELETE = ".delete";

  /**
   * 更新
   */
  final String SQL_UPDATE = ".update";

  /**
   * 根据Id查询
   */
  final String SQL_SELECT_SINGLE = ".selectSingle";

  @Resource
  protected M mapper;

  @Resource
  protected SqlSessionTemplate session;

  /**
   * 新規
   * 
   * @param entity
   * @return
   * @author kindustry
   */
  @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
  @SuppressWarnings("unchecked")
  public boolean save(T... entities) {
    boolean flag = true;
    if (null == entities || entities.length == 0) {
      logger.error("参数对象为null!");
      throw new IllegalArgumentException("参数不可为null！");
    }
    for (T entity : entities) {
      flag = flag & mapper.save(entity);
    }
    return flag;
  }

  /**
   * 根據主鍵刪除
   * 
   * @param entities
   * @return 刪除記錄數
   * @author kindustry
   */
  @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
  @SuppressWarnings("unchecked")
  public int deleteByPk(T... entities) {
    int count = 0;
    if (null == entities || entities.length == 0) {
      logger.error("参数对象为null!");
      throw new IllegalArgumentException("参数不可为null！");
    }
    for (T entity : entities) {
      count = count + mapper.deleteByPk(entity);
    }
    return count;
  }

  /**
   * 根據模板删除记录
   * 
   * @param entities
   * @param includeColumns
   * @return
   * @author kindustry
   */
  public int deleteByExample(T entity, String... includeColumns) {
    List<T> entities = new ArrayList<T>();
    entities.add(entity);
    return deleteByExample(entities, includeColumns);
  }

  /**
   * 根據模板删除记录
   * 
   * @param entities
   * @return
   * @author kindustry
   */
  @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
  public int deleteByExample(List<T> entities, String... includeColumns) {
    int count = 0;
    if (null == entities || entities.size() == 0) {
      logger.error("参数对象为null!");
      throw new IllegalArgumentException("参数不可为null！");
    }
    for (T entity : entities) {
      count = count + mapper.deleteByExample(entity, includeColumns);
    }
    return count;
  }

  public boolean update(T entity) {
    if (null == entity) {
      throw new IllegalArgumentException("参数不可为null！");
    }
    try {
      session.update(entity.getClass().getName() + ".update", entity);
      session.commit();
      return true;
    } catch (Exception e) {
      logger.error("更新数据异常：", e);
      session.rollback();
    } finally {
      session.close();
    }
    return false;
  }

  // @Override
  // public boolean delete(Object param) {
  // try {
  // getSqlSession().delete(entityClass.getName() + SQL_DELETE, param);
  // } catch (Exception e) {
  // logger.error("删除数据异常：", e);
  // return false;
  // }
  // return true;
  // }

  public void wipe(Class entityClass) {
    try {
      session.delete(entityClass.getName() + ".truncate");
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
      session.rollback();
    } finally {
      session.close();
    }
  }

  public T selectSingle(T entity) {
    if (null == entity) {
      logger.error("非法参数：param为空！");
      throw new IllegalArgumentException("非法参数：param为空！");
    }
    T result = null;
    try {
      result = session.selectOne(entity.getClass().getName() + SQL_SELECT_SINGLE, entity);
    } catch (Throwable e) {
      logger.error("{}", e);
    }
    return result;
  }

  public T findById(Class<T> entityClass, int id) {
    try {
      return session.selectOne(entityClass.getName() + ".findById", id);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      session.close();
    }
    return null;
  }

  public List findAll(Class<T> entityClass) {
    try {
      return session.selectList(entityClass.getName() + ".findAll");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      session.close();
    }
    return null;
  }

  public <T> List<T> query(String _mybitsId, Map<String, Object> _params) {
    return session.selectList(_mybitsId, _params);
  }

  public <T> List<T> query(String _mybitsId, Object _params) {
    return session.selectList(_mybitsId, _params);
  }

  public Object queryOne(String _mybitsId, Object object) {
    return session.selectOne(_mybitsId, object);
  }

  // public boolean addBatch(List<Object> entities) throws Exception {
  // // TODO Auto-generated method stub
  // int result = 1;
  // SqlSession batchSqlSession = null;
  // try {
  // // 获取批量方式的sqlsession
  // batchSqlSession =
  // this.getSqlSessionTemplate().getSqlSessionFactory().openSession(ExecutorType.BATCH,
  // false);
  // int batchCount = 1000; // 每批commit的个数
  // int batchLastIndex = batchCount; // 每批最后一个的下标
  // for (int index = 0; index < members.size();) {
  // if (batchLastIndex >= members.size()) {
  // batchLastIndex = members.size();
  // result = result *
  // batchSqlSession.insert("MutualEvaluationMapper.insertCrossEvaluation",
  // members.subList(index, batchLastIndex));
  // batchSqlSession.commit();
  // System.out.println("index:" + index + " batchLastIndex:" + batchLastIndex);
  // break;// 数据插入完毕，退出循环
  // } else {
  // result = result *
  // batchSqlSession.insert("MutualEvaluationMapper.insertCrossEvaluation",
  // members.subList(index, batchLastIndex));
  // batchSqlSession.commit();
  // System.out.println("index:" + index + " batchLastIndex:" + batchLastIndex);
  // index = batchLastIndex; // 设置下一批下标
  // batchLastIndex = index + (batchCount - 1);
  // }
  // }
  // batchSqlSession.commit();
  // } finally {
  // batchSqlSession.close();
  // }
  // return Tools.getBoolean(result);
  // }

  /*
   * public PagerVO findPaginated(String sqlId, Map params) { SqlSession session
   * = MyBatisUtil.getSession(); List datas = null; int total = 0; try {
   * 
   * // 取出分页参数，设置到params中 // params.put("offset", SystemContext.getOffset()); //
   * params.put("pagesize", SystemContext.getPagesize());
   * 
   * datas = session.selectList(sqlId, params); total = (Integer)
   * session.selectOne(sqlId + "-count", params); } catch (Exception e) {
   * e.printStackTrace(); } finally { session.close(); } PagerVO pv = new
   * PagerVO(); pv.setDatas(datas); pv.setTotal(total); return pv; }
   */

}