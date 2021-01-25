package com.kindustry.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * JSON转换工具类 public static final Object parse(String text); //
 * 把JSON文本parse为JSONObject或者JSONArray public static final JSONObject
 * parseObject(String text)； // 把JSON文本parse成JSONObject public static final T
 * parseObject(String text, Class clazz); // 把JSON文本parse为JavaBean public static
 * final JSONArray parseArray(String text); // 把JSON文本parse成JSONArray public
 * static final List parseArray(String text, Class clazz);
 * //把JSON文本parse成JavaBean集合 public static final String toJSONString(Object
 * object, boolean prettyFormat); // 将JavaBean序列化为带格式的JSON文本 public static final
 * Object toJSON(Object javaObject); 将JavaBean转换为JSONObject或者JSONArray。
 */
public class JsonUtil {

  /**
   * 对象转换成JSON字符串
   * 
   * public static final String toJSONString(Object object); //
   * 将JavaBean序列化为JSON文本
   * 
   * @param obj
   *          需要转换的对象
   * @return 对象的string字符
   */
  public static final String toJSONString(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    String json = null;
    try {
      json = mapper.writeValueAsString(obj);
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return json;
  }

  /**
   * 对象转换成JSON字符串
   * 
   * @param obj
   *          需要转换的对象
   * @return 对象的string字符
   */
  public static String toJSONString(String key, Object obj) {
    Map map = new HashMap();
    map.put(key, obj);
    return toJSONString(map);
  }

  /**
   * JSON字符串转换成对象
   * 
   * @param jsonContent
   *          需要转换的字符串
   * @param type
   *          需要转换的对象类型
   * @return 对象
   */
  public static <T> T parse(String jsonContent, Class<T> type) {
    // JSONObject jsonObject = JSONObject.fromObject(jsonString);
    // return (T) JSONObject.toBean(jsonObject, type);
    /** 
     * ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化。 
     */  
    ObjectMapper mapper = new ObjectMapper();  
    T bean = null ;
    try {
      bean = mapper.readValue(jsonContent, type);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }  
    return bean;
  }

  /**
   * 将JSONArray对象转换成list集合
   * 
   * @param jsonArr
   * @return
   */
  // public static List<Object> jsonToList(JSONArray jsonArr) {
  // List<Object> list = new ArrayList<Object>();
  // for (int i = 0; i < jsonArr.length(); i++) {
  // Object obj = jsonArr.get(i);
  // if (obj instanceof JSONArray) {
  // list.add(jsonToList((JSONArray) obj));
  // } else if (obj instanceof JSONObject) {
  // list.add(jsonToMap((JSONObject) obj));
  // } else {
  // list.add(obj);
  // }
  // }
  // return list;
  // }

  /**
   * 将json字符串转换成map对象
   * 
   * @param json
   * @return
   */
  public static Map<String, Object> jsonToMap(String json) {
    return null;
  }

  /**
   * 将JSONObject转换成map对象
   * 
   * @param json
   * @return
   */
//  public static Map<String, Object> jsonToMap(JSONObject obj) {
//    Set<String> set = obj.keySet();
//    Map<String, Object> map = new HashMap<String, Object>(set.size());
//    for (String key : set) {
//      Object value = obj.get(key);
//      if (value instanceof JSONArray) {
//        map.put(key.toString(), jsonToList((JSONArray) value));
//      } else if (value instanceof JSONObject) {
//        map.put(key.toString(), jsonToMap((JSONObject) value));
//      } else {
//        map.put(key.toString(), obj.get(key));
//      }
//
//    }
//    return map;
//  }
  
  
  
  
}