package com.kindustry.framework.cache;
/**
 * 用于拦截器自动处理缓存
 * */
public interface ICacheSupport {

	/**
	 * 得到缓存的key
	 * */
	String getCacheKey();
	
	/**得到缓存的名称（即使用的缓存类别）*/
	String getCacheName();
	
	/**本次请求是否使用缓存数据*/
	boolean isUseCacheData();
	
	/**为该对象提供缓存数据*/
	void supportCache (Object o);
	
	/**准备需要被缓存的对象*/
	Object prepareCache ();
	
	/**当缓存命中时返回Action的Result*/
	String onCacheHitResult();
	
}
