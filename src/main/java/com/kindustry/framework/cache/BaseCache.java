package com.kindustry.framework.cache;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-7-16
 * Time: 下午3:35
 * To change this template use File | Settings | File Templates.
 */
public interface BaseCache {
    public void set(String key, Object value) throws Exception;

    public void set(Map<String, Object> mapping) throws Exception;

    public Object get(String key) throws Exception;

    public Object[] get(String key[]) throws Exception;

    public void remove(String key) throws Exception;
}
