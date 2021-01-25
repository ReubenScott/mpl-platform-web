package com.kindustry.common.cache.factory;

import com.kindustry.common.cache.IBaseCache;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-7-16
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public interface CacheFactory {
    public IBaseCache getCache(String name);

    public void removeCache(String name);
}
