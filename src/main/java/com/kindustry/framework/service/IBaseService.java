package com.kindustry.framework.service;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.kindustry.system.entity.Menu;

public interface IBaseService {

  // 查询用户菜单
  public List<Menu> findMenuByUser(String uid);

  // 返回所有的 缓存的 key
  public <T> List<T> getCacheKeys(String cacheName);

  // 检查是否缓存
  public boolean contains(String cacheName, Object o);

  // 获取缓存对象
  public <T> T getCacheBean(String cacheName, String key);

  public void putCacheBean(String cacheName, String key, Object value);

  // 清除某个缓存
  public void cacheEvict(String cacheName, String key);

  // Excel 下载
  public Workbook createExcelBySQL(String title, String sql, Object... params);

  public boolean deleteEntityBySID(Class entity, Serializable sid);

  public boolean deleteAnnotatedEntity(Object annoEntity);

}