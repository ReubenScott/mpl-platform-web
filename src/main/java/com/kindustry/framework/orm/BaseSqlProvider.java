package com.kindustry.framework.orm;

/**
 * 数据库访问标识接口
 * 
 * @author TonyJ
 *@time 2015-1-31 下午03:41:17
 *@email tanglongjia@126.com
 */
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

public class BaseSqlProvider {

  public <PK> String isExisted(PK key) throws Exception {
    String tableName = getTableName(key);
    SQL sql = new SQL();
    sql.SELECT("1");
    sql.FROM(tableName);

    Class<? extends Object> clazz = key.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

      try {
        Object value = clazz.getMethod(getMethod).invoke(key);
        if (null != value && !(value instanceof Integer && (Integer) value == -999)) {
          sql.WHERE(fieldName + " = " + "#{" + fieldName + "}");
        } else {
          // TODO ：chenz App.MySetting.Error
        }
      } catch (Exception ex) {
        throw new Exception(ex);
      }
    }
    sql.FETCH_FIRST_ROWS_ONLY(1);
    return sql.toString();
  }

  public <TP> String insert(TP entity) throws Exception {
    String tableName = getTableName(entity);

    SQL sql = new SQL();
    sql.INSERT_INTO(tableName);

    Class<? extends Object> clazz = entity.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

      try {
        Object value = clazz.getMethod(getMethod).invoke(entity);
        if (null != value && !(value instanceof Integer && (Integer) value == -999)) {
          sql.INTO_COLUMNS(fieldName);
          sql.INTO_VALUES("#{" + fieldName + "}");
        }
      } catch (Exception ex) {
        throw new Exception(ex);
      }
    }

    Class<?> superClazz = clazz.getSuperclass();
    if (!superClazz.equals(Object.class)) {
      Field[] superFields = superClazz.getDeclaredFields();
      for (Field field : superFields) {
        String fieldName = field.getName();
        String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

        try {
          if (null != clazz.getMethod(getMethod).invoke(entity)) {
            sql.INTO_COLUMNS(fieldName);
            sql.INTO_VALUES("#{" + fieldName + "}");
          }
        } catch (Exception ex) {
          throw new Exception(ex);
        }
      }
    }

    return sql.toString();
  }

  public <TP> String update(TP entity, HashSet<String> excludeColumns) throws Exception {
    String tableName = getTableName(entity);

    SQL sql = new SQL();
    sql.UPDATE(tableName);

    Class<? extends Object> clazz = entity.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      if (excludeColumns.contains(fieldName)) {
        continue;
      }
      String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

      try {
        Object value = clazz.getMethod(getMethod).invoke(entity);
        if (null != value && !(value instanceof Integer && (Integer) value == -999)) {
          sql.SET(fieldName + " = " + "#{" + "entity" + "." + fieldName + "}");
        }
      } catch (Exception ex) {
        throw new Exception(ex);
      }
    }

    Class<?> superClazz = clazz.getSuperclass();
    if (!superClazz.equals(Object.class)) {
      Field[] superFields = superClazz.getDeclaredFields();
      for (Field field : superFields) {
        String fieldName = field.getName();
        String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

        try {
          if (null != clazz.getMethod(getMethod).invoke(entity)) {
            sql.WHERE(fieldName + " = " + "#{" + "entity" + "." + fieldName + "}");
          }
        } catch (Exception ex) {
          throw new Exception(ex);
        }
      }
    }

    return sql.toString();
  }

  public <PK> String get(PK key) throws Exception {
    String tableName = getTableName(key);
    SQL sql = new SQL();
    sql.SELECT("*");
    sql.FROM(tableName);

    Class<? extends Object> clazz = key.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

      try {
        if (null != clazz.getMethod(getMethod).invoke(key)) {
          sql.WHERE(fieldName + " = " + "#{" + fieldName + "}");
        }
      } catch (Exception ex) {
        throw new Exception(ex);
      }
    }
    return sql.toString();
  }

  private <OBJ> String getTableName(OBJ obj) {
    String tableName = obj.getClass().getSimpleName();
    if (tableName.contains("KeyEntity")) {
      tableName = tableName.replace("KeyEntity", "");
    } else {
      tableName = tableName.replace("Entity", "");
    }

    return tableName;
  }

  public String delete(Map<String, Object> map) {
    String tName = (String) map.get("tableName");
    SQL sql = new SQL();
    sql.DELETE_FROM(tName);
    for (String fieldName : map.keySet()) {
      if (fieldName == "tableName") {
        continue;
      }
      // sql.WHERE(fieldName + "=" + map.get(fieldName).toString());
      sql.WHERE(fieldName + " = " + "#{" + fieldName + "}");
    }
    return sql.toString();
  }

}
