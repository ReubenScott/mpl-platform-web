package com.kindustry.orm.jdbc;

/**
 * 数据库访问标识接口
 * 
 * @author TonyJ
 * @time 2015-1-31 下午03:41:17
 * @email tanglongjia@126.com
 */
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.jdbc.SQL;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlProvider {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 獲取表名
   * 
   * @param obj
   * @return
   * @author kindustry
   */
  private <T> String getTableName(T entity) {
    // 获取类的字节码
    Class<? extends Object> clazz = entity.getClass();

    // 从字节码上获取name的值，这个值就是表名
    Table table = clazz.getAnnotation(Table.class);
    if (table == null) {
      logger.error("Entity : {} have not correct configed  [@Table]", clazz.getName());
      throw new IllegalArgumentException("Entity : " + clazz.getName() + " have not configed [@Table]");
    }

    // 获取表名
    String tableName = table.name();

    if (tableName.length() == 0) {
      tableName = clazz.getSimpleName();
    }

    return tableName;
  }

  /**
   * 新規
   * 
   * @param entity
   * @return
   * @throws Exception
   * @author kindustry
   */
  public <TP> String insert(TP entity) throws Exception {
    String tableName = getTableName(entity);

    SQL sql = new SQL();
    sql.INSERT_INTO(tableName);

    Class<? extends Object> clazz = entity.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      if ("serialVersionUID".equals(fieldName)) {
        continue;
      }
      String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

      try {
        Object value = clazz.getMethod(getMethod).invoke(entity);
        if (null != value && !(value instanceof Integer && (Integer)value == -999)) {
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

  /**
   * 根據主鍵刪除
   * 
   * @param map
   * @return
   * @author kindustry
   */
  @SuppressWarnings("hiding")
  public <T> String deleteByPk(T entity) {
    int count = 0;
    SQL sql = new SQL();
    sql.DELETE_FROM(getTableName(entity));

    // 获取类的字节码
    Class<? extends Object> clazz = entity.getClass();

    // 获取所有声明的字段
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      if ("serialVersionUID".equals(fieldName)) {
        continue;
      }

      // 註解
      Column[] columns = field.getAnnotationsByType(Column.class);
      Id[] ids = field.getAnnotationsByType(Id.class);
      if (ids.length > 0) {
        if (columns.length == 0) {
          logger.error("Column : {}.{} have not configed  [@Column]", clazz.getName(), fieldName);
          throw new IllegalArgumentException("Column : " + clazz.getName() + "." + fieldName + " have not configed [@Column]");
        }

        for (Column column : columns) {
          String columnName = column.name();
          sql.WHERE(columnName + " = " + "#{" + fieldName + "}");
          count++;
        }
      }

    }

    // Superclass
    clazz = clazz.getSuperclass();
    if (!clazz.equals(Object.class)) {
      Field[] superFields = clazz.getDeclaredFields();
      for (Field field : superFields) {
        String fieldName = field.getName();
        if ("serialVersionUID".equals(fieldName)) {
          continue;
        }

        // 註解
        Column[] columns = field.getAnnotationsByType(Column.class);
        Id[] ids = field.getAnnotationsByType(Id.class);
        if (ids.length > 0) {
          if (columns.length == 0) {
            logger.error("Column : {}.{} have not configed  [@Column]", clazz.getName(), fieldName);
            throw new IllegalArgumentException("Column : " + clazz.getName() + "." + fieldName + " have not configed [@Column]");
          }

          for (Column column : columns) {
            String columnName = column.name();
            sql.WHERE(columnName + " = " + "#{" + fieldName + "}");
            count++;
          }
        }

      }
    }

    // 沒注解 主鍵 @Id
    if (count == 0) {
      logger.error("Entity : {}  have no field configed Primary Key [@Id]", clazz.getName());
      throw new IllegalArgumentException("Entity : " + clazz.getName() + " have no field configed Primary Key [@Id]");
    }

    return sql.toString();
  }

  /**
   * 根據模版刪除
   * 
   * @param entity
   * @param includeColumns
   * @return
   * @author kindustry
   */
  public String deleteByExample(T entity, String... includeColumns) {
    int count = 0;
    SQL sql = new SQL();
    sql.DELETE_FROM(getTableName(entity));

    // 获取类的字节码
    Class<? extends Object> clazz = entity.getClass();

    // 获取所有声明的字段
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      if ("serialVersionUID".equals(fieldName) || !Arrays.asList(includeColumns).contains(fieldName)) {
        continue;
      }

      // 註解
      Column[] columns = field.getAnnotationsByType(Column.class);
      if (columns.length == 0) {
        logger.error("Column : {}.{} have not configed  [@Column]", clazz.getName(), fieldName);
        throw new IllegalArgumentException("Column : " + clazz.getName() + "." + fieldName + " have not configed [@Column]");
      }
      for (Column column : columns) {
        String columnName = column.name();
        sql.WHERE(columnName + " = " + "#{" + fieldName + "}");
        count++;
      }

    }

    // Superclass
    clazz = clazz.getSuperclass();
    if (!clazz.equals(Object.class)) {
      Field[] superFields = clazz.getDeclaredFields();
      for (Field field : superFields) {
        String fieldName = field.getName();
        if ("serialVersionUID".equals(fieldName) || !Arrays.asList(includeColumns).contains(fieldName)) {
          continue;
        }

        // 註解
        Column[] columns = field.getAnnotationsByType(Column.class);
        if (columns.length == 0) {
          logger.error("Column : {}.{} have not configed  [@Column]", clazz.getName(), fieldName);
          throw new IllegalArgumentException("Column : " + clazz.getName() + "." + fieldName + " have not configed [@Column]");
        }

        for (Column column : columns) {
          String columnName = column.name();
          sql.WHERE(columnName + " = " + "#{" + fieldName + "}");
          count++;
        }

      }

    }

    // 沒注解 @Column
    if (count == 0) {
      logger.error("Entity : {}  have no field configed [@Column]", clazz.getName());
      throw new IllegalArgumentException("Entity : " + clazz.getName() + " have no field configed [@Column]");
    }

    return sql.toString();
  }

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
        if (null != value && !(value instanceof Integer && (Integer)value == -999)) {
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

  public <TP> String update(TP entity, Set<String> excludeColumns) throws Exception {
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
        if (null != value && !(value instanceof Integer && (Integer)value == -999)) {
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

  /**
   * 
   * @param key
   * @return
   * @throws Exception
   * @author kindustry
   */
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

}
