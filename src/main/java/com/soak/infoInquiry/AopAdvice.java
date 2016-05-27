package com.soak.infoInquiry;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;
import com.soak.framework.cache.BaseCache;
import com.soak.framework.cache.ICacheSupport;
import com.soak.framework.cache.factory.EhCacheFactory;

/***
 * 切面
 * @todo
 * @time 2013-4-22
 * @author gml
 *
 */
public class AopAdvice implements MethodBeforeAdvice {
  
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private BaseCache cache;

  public void before(Method method, Object[] args, Object target)
      throws Throwable {
    
    System.out.println("before mothod"); 
    System.out.println(method);
    System.out.println(args);
    System.out.println(target);
    
    if (target instanceof ICacheSupport ) {
      ICacheSupport isc = (ICacheSupport)target;
      logger.debug("The target Action <" + target.getClass().getName() + "> has enabled cache <" + isc.getCacheName() + ">.");
      cache = EhCacheFactory.getInstance().getCache(isc.getCacheName());
      Object obj = cache.get(isc.getCacheKey());
      if (obj == null) {
        // 缓存未命中
        logger.debug("cache key <" + isc.getCacheKey() + "> did not hit.");
        cache.set(isc.getCacheKey(), isc.prepareCache());
        // 缓存未命中
        logger.debug("cache key <" + isc.getCacheKey() + "> has stored.");
      } else if (isc.isUseCacheData()) {
        // 缓存命中
        logger.debug("cache key <" + isc.getCacheKey() + "> has hit.");
        isc.supportCache(obj);
      } else {
        // 缓存命中，但是被拒绝，更新缓存
        logger.debug("cache key <" + isc.getCacheKey() + "> has hit.But this request drops it.");
        cache.set(isc.getCacheKey(), isc.prepareCache());
        // 更新缓存
        logger.debug("cache key <" + isc.getCacheKey() + "> has been refreshed.");
      }
    }
    
  }
  

  
  
}