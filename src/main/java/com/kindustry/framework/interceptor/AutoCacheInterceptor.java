package com.kindustry.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kindustry.framework.cache.BaseCache;
import com.kindustry.framework.cache.ICacheSupport;
import com.kindustry.framework.cache.factory.EhCacheFactory;
import com.kindustry.framework.config.Configure;

/**
 * 自动缓存拦截器 功能：拦截实现了{@link ICacheSupport}接口的Action，如果缓存命中，则从缓存中返回结果
 */
public class AutoCacheInterceptor implements HandlerInterceptor {

  private static final long serialVersionUID = -5175655267725129422L;

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final Configure conf = Configure.getInstance();
  private static final String CACHE_CONF = "globalCache";
  private static final String CACHE_OPEN = "enable";
  private static final String CACHE_CLOSE = "disable";

  private BaseCache cache;

  @Override
  public boolean preHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject) throws Exception {
    System.out.println("preHandle");
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, ModelAndView paramModelAndView)
      throws Exception {
    // TODO Auto-generated method stub

    System.out.println("postHandle");

    Object action = paramObject;

    if (action instanceof ICacheSupport && isCacheEnable()) {
      ICacheSupport isc = (ICacheSupport) paramObject;
      logger.debug("The target Action <" + action.getClass().getName() + "> has enabled cache <" + isc.getCacheName() + ">.");
      cache = EhCacheFactory.getInstance().getCache(isc.getCacheName());
      Object obj = cache.get(isc.getCacheKey());
      if (obj == null) {
        // 缓存未命中
        logger.debug("cache key <" + isc.getCacheKey() + "> did not hit.");
        // String result = inv.invoke();
        cache.set(isc.getCacheKey(), isc.prepareCache());
        // 缓存未命中
        logger.debug("cache key <" + isc.getCacheKey() + "> has stored.");
        // return result;
      } else if (isc.isUseCacheData()) {
        // 缓存命中
        logger.debug("cache key <" + isc.getCacheKey() + "> has hit.");
        isc.supportCache(obj);
        isc.onCacheHitResult();
      } else {
        // 缓存命中，但是被拒绝，更新缓存
        logger.debug("cache key <" + isc.getCacheKey() + "> has hit.But this request drops it.");
        // String result = inv.invoke();
        cache.set(isc.getCacheKey(), isc.prepareCache());
        // 更新缓存
        logger.debug("cache key <" + isc.getCacheKey() + "> has been refreshed.");
        // return result;
      }
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    System.out.println("afterCompletion");
  }

  /*
   * public String intercept(ActionInvocation inv) throws Exception {
   * 
   * Object action = inv.getAction();
   * 
   * if (action instanceof ICacheSupport && isCacheEnable()) { ICacheSupport isc
   * = (ICacheSupport) inv.getAction(); logger.debug("The target Action <" +
   * action.getClass().getName() + "> has enabled cache <" + isc.getCacheName()
   * + ">."); cache = EhCacheFactory.getInstance().getCache(isc.getCacheName());
   * Object obj = cache.get(isc.getCacheKey()); if (obj == null) { // 缓存未命中
   * logger.debug("cache key <" + isc.getCacheKey() + "> did not hit."); String
   * result = inv.invoke(); cache.set(isc.getCacheKey(), isc.prepareCache()); //
   * 缓存未命中 logger.debug("cache key <" + isc.getCacheKey() + "> has stored.");
   * return result; } else if (isc.isUseCacheData()) { // 缓存命中
   * logger.debug("cache key <" + isc.getCacheKey() + "> has hit.");
   * isc.supportCache(obj); return isc.onCacheHitResult(); } else { //
   * 缓存命中，但是被拒绝，更新缓存 logger.debug("cache key <" + isc.getCacheKey() +
   * "> has hit.But this request drops it."); String result = inv.invoke();
   * cache.set(isc.getCacheKey(), isc.prepareCache()); // 更新缓存
   * logger.debug("cache key <" + isc.getCacheKey() + "> has been refreshed.");
   * return result; } } return inv.invoke(); }
   */

  /** 全局策略中缓存是否启用 */
  private boolean isCacheEnable() {
    if (CACHE_OPEN.equals(conf.getValue(CACHE_CONF))) {
      return true;
    }
    logger.debug("The target Action has enabled cache.But global config was closed.");
    return false;
  }

}
