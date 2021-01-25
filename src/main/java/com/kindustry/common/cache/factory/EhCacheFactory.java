package com.kindustry.common.cache.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kindustry.common.cache.EhCacheImpl;
import com.kindustry.common.cache.IBaseCache;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-7-16
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class EhCacheFactory implements CacheFactory {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private static CacheFactory instance = new EhCacheFactory();
  private static Map<String, IBaseCache> cacheMapping = new ConcurrentHashMap<String, IBaseCache>();
  private CacheManager manager;

  public static CacheFactory getInstance() {
    return instance;
  }

  private EhCacheFactory() {
    manager = CacheManager.getInstance();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        manager.shutdown();
        logger.info("EhCacheFactory instance shutdown");
      }
    });
  }

  public IBaseCache getCache(String name) {
    if (name == null || name.length() == 0) {
      return null;
    }
    IBaseCache cache = cacheMapping.get(name);
    if (cache == null) {
      if (!manager.cacheExists(name)) {
        manager.addCache(name);
      }
      Cache ehCache = manager.getCache(name);
      cache = new EhCacheImpl(ehCache, name);
      cacheMapping.put(name, cache);
    }
    return cache;
  }

  public void removeCache(String name) {
    if (name == null || name.length() == 0) {
      return;
    }
    if (cacheMapping.containsKey(name)) {
      cacheMapping.remove(name);
    }
    if (manager.cacheExists(name)) {
      manager.removeCache(name);
    }
  }
}
