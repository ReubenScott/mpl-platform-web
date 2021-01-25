package com.kindustry.framework.service.imp;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import net.sf.ehcache.Element;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import com.kindustry.framework.dao.IBaseDao;
import com.kindustry.framework.service.IBaseService;
import com.kindustry.system.entity.Menu;

public class BaseServiceImp implements IBaseService {

  @Resource
  protected IBaseDao baseDao;

  public IBaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(IBaseDao baseDao) {
    this.baseDao = baseDao;
  }

  /**
   * 获取对象
   * 
   * @param beanName
   * @return
   */
  public <T> T getBean(String beanName) {
    // return (T) ApplicationListener.getBean(beanName);
    return null;
  }

  /**
   * 获取缓存对象
   * 
   * @param <T>
   * @param cacheName
   * @param key
   * @return
   */
  public <T> T getCacheBean(String cacheName, String key) {
    EhCacheCacheManager cacheManager = this.getBean("cacheManager");
    Cache cache = cacheManager.getCache(cacheName);
    ValueWrapper element = cache.get(key);
    return element == null ? null : (T) element.get();
  }

  /**
   * 
   * @param cacheName
   * @param key
   * @param value
   */
  public void putCacheBean(String cacheName, String key, Object value) {
    EhCacheCacheManager cacheManager = this.getBean("cacheManager");
    Cache cache = cacheManager.getCache(cacheName);
    cache.put(key, value);
  }

  public boolean contains(String cacheName, Object o) {
    EhCacheCacheManager cacheManager = this.getBean("cacheManager");
    net.sf.ehcache.Ehcache cache = (net.sf.ehcache.Ehcache) cacheManager.getCache(cacheName).getNativeCache();
    for (Object key : cache.getKeys()) {
      Element element = cache.get(key);
      if (element != null && element.getObjectValue().equals(o)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 返回所有的 缓存的 key
   * 
   * @param cacheName
   * @return
   */
  public <T> List<T> getCacheKeys(String cacheName) {
    EhCacheCacheManager cacheManager = this.getBean("cacheManager");
    net.sf.ehcache.Ehcache cache = (net.sf.ehcache.Ehcache) cacheManager.getCache(cacheName).getNativeCache();
    return cache.getKeys();
  }

  /**
   * 清除某个缓存
   * 
   * @param cacheName
   * @param key
   * @return
   */
  public void cacheEvict(String cacheName, String key) {
    EhCacheCacheManager cacheManager = this.getBean("cacheManager");
    Cache cache = cacheManager.getCache(cacheName);
    cache.evict(key);
  }

  /**
   * Excel 2007 下载
   * 
   * @param fileName
   * @param sql
   * @param params
   */
  public Workbook createExcelBySQL(String title, String sql, Object... params) {
    // return basicDao.exportExcel(null, title, sql, params);
    return null;
  }

  /**
   * 获取用户菜单
   */
  public List<Menu> findMenuByUser(String xml) {
    Menu menu = new Menu();
    // return basicDao.findByAnnotatedSample(menu);
    return null;
  }

  /**
   * 
   * @param sid
   */
  public boolean deleteEntityBySID(Class entity, Serializable sid) {
    // return basicDao.deleteEntityBySID(entity, sid);
    return false;
  }

  /**
   * 
   * @param sid
   */
  public boolean deleteAnnotatedEntity(Object annoEntity) {
    // return basicDao.deleteAnnotatedEntity(annoEntity);
    return false;
  }

}