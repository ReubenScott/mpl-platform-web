package com.kindustry.framework.cache.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import com.kindustry.framework.cache.BaseCache;
import com.kindustry.framework.cache.EhCacheImpl;


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
    private static Map<String, BaseCache> cacheMapping = new ConcurrentHashMap<String, BaseCache>();
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

    public BaseCache getCache(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        BaseCache cache = cacheMapping.get(name);
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
