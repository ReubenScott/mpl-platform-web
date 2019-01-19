package com.kindustry.framework.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-7-16
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 */
public class EhCacheImpl implements BaseCache {
    private Cache ehcache;
    private String name;
    
    public EhCacheImpl(){
      
    }

    public EhCacheImpl(Cache ehcache, String name) {
        this.ehcache = ehcache;
        this.name = name;
    }

    public void set(String key, Object value) {
        Element element = new Element(key, value);
        this.ehcache.put(element);
    }

    public void set(Map<String, Object> mapping){
        Iterator<String> it = mapping.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object value = mapping.get(key);
            this.set(key, value);
        }
    }

    public Object get(String key) {
        Element element = this.ehcache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    public Object[] get(String[] key)  {
        if (key == null || key.length == 0) {
            return null;
        }
        Object[] result = new Object[key.length];
        for (int i = 0; i < key.length; i++) {
            result[i] = this.get(key[i]);
        }
        return result;
    }

    public void remove(String key) {
        ehcache.remove(key);
    }
}
