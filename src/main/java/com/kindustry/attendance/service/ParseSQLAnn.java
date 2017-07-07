package com.kindustry.attendance.service;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

import com.kindustry.attendance.model.AtndSummarySheet;
import com.kindustry.common.date.DateUtil;

public class ParseSQLAnn {

  public static String query(Object obj) {
    StringBuffer sb = new StringBuffer();
    sb.append("select * from ");
    // 1、获得obj对应的class
    Class c = obj.getClass();
    // 2、找到类上的注解
    boolean isExit;
    isExit = c.isAnnotationPresent(Table.class);
    if (!isExit) {
      return null;
    }
    Table tab = (Table) c.getAnnotation(Table.class);
    String tableName = tab.name();
    sb.append(tableName).append(" where 1=1");
    // 3、获取字段名
    Field[] fs = c.getDeclaredFields();
    for (Field f : fs) {
      isExit = f.isAnnotationPresent(Column.class);
      if (!isExit) {
        continue;
      }
      Column col = (Column) f.getAnnotation(Column.class);
      String columnName = col.name();
      String fieldName = f.getName();
      // 4、获取字段值，反射机制
      String getName = "get";
      getName = getName + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Object fieldValue = null;
      try {
        Method m = c.getMethod(getName);
        fieldValue = m.invoke(obj);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (fieldValue == null || (fieldValue instanceof Integer && (Integer) fieldValue == 0)) {
        continue;
      } else {
        sb.append(" and ").append(columnName);
        if (fieldValue instanceof String) {
          if (((String) fieldValue).contains(",")) {
            String[] ss = ((String) fieldValue).split(",");
            sb.append(" in(");
            for (String s : ss) {
              sb.append("'").append(s).append("'").append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
          } else {
            sb.append(" = '").append(fieldValue).append("'");
          }
        } else if (fieldValue instanceof Integer) {
          sb.append(" = ").append((Integer) fieldValue);
        }
      }
    }
    return sb.toString();

  }

  public static String QueryStu(Object stu) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    StringBuilder sb = new StringBuilder("select * from ");

    // 获取类的class
    Class stuClass = stu.getClass();

    /* 通过获取类的类注解，来获取类映射的表名称 */
    if (stuClass.isAnnotationPresent(Table.class)) { // 如果类映射了表
      Table table = (Table) stuClass.getAnnotation(Table.class);
      sb.append(table.name() + " where 1=1 "); // 加入表名称

      /* 遍历所有的字段 */
      Field[] fields = stuClass.getDeclaredFields();// 获取类的字段信息
      for (Field field : fields) {
        if (field.isAnnotationPresent(Column.class)) {
          Column col = field.getAnnotation(Column.class); // 获取列注解
          String fieldName = field.getName(); // 获取字段名称
          String MethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);// 获取字段的get方法
          Method method = stuClass.getMethod(MethodName); // get到field的值

          /* 空字段跳过拼接过程。。。 */
          if (method.invoke(stu) == null || (method.invoke(stu) instanceof Integer && (Integer) method.invoke(stu) == 0)) { // 如果没有值，不拼接
            continue;
          }

          /* 通过函数的返回值类型来判断我强制转换时候该转换成什么类型 */
          if (method.getReturnType() == String.class) {
            String fieldValue = (String) method.invoke(stu, null);
            sb.append(" and " + fieldName + "=" + "'" + fieldValue + "'");
          } else if (method.getReturnType() == int.class) {
            Integer fieldValue = (Integer) method.invoke(stu, null);
            sb.append(" and " + fieldName + "=" + fieldValue);
          } else if (method.getReturnType() == Date.class) {
            Date fieldValue = (Date) method.invoke(stu, null);
            sb.append(" and " + fieldName + "=" + "'" + fieldValue + "'");
          }

          // 根据情况else 其他数据类型，看着玩儿吧....

        } else {
          continue;
        }
      }
    } else {
      return null;
    }
    return sb.toString();
  }

  /**
   * 通过实体类生成 insert into sql语句
   * 
   * @param cl
   *          <a href="http://my.oschina.net/u/556800" class="referer" target="_blank">@return</a>
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws NumException
   */
  public static String save(Object cl) throws IllegalArgumentException, IllegalAccessException, Exception {
    String sql = "insert into ";
    if (cl != null) {
      Field[] fiels = cl.getClass().getDeclaredFields();// 获得反射对象集合
      boolean t = cl.getClass().isAnnotationPresent(Table.class);// 获得类是否有注解
      if (t) {
        Table tab = cl.getClass().getAnnotation(Table.class);
        // sql += tab.name();// 获得表名
        String name = "";// 记录字段名
        String value = "";// 记录值名称
        boolean bl = false;// 记录主键是否为空
        for (Field fl : fiels) {// 循环组装
          fl.setAccessible(true);// 开启支私有变量的访问权限
          Object tobj = fl.get(cl);
          if (tobj != null) {
            // if (fl.isAnnotationPresent(Key.class)) {// 判断是否存在主键
            // bl = true;
            // }
            // if (!fl.isAnnotationPresent(notRecord.class)) {
            // name += fl.getName() + ",";
            // value += "'" + tobj.toString() + "',";
            // }
          }
        }
        if (bl) {
          if (name.length() > 0)
            name = name.substring(0, name.length() - 1);
          if (value.length() > 0)
            value = value.substring(0, value.length() - 1);
          sql += "(" + name + ") values(" + value + ")";
        } else
          throw new Exception("未找到类主键 主键不能为空");
      } else
        throw new Exception("传入对象不是实体类");
    } else
      throw new Exception("传入对象不能为空");// 抛出异常
    return sql;
  }

  /**
   * 传入对象更新
   * 
   * @param obj
   *          <a href="http://my.oschina.net/u/556800" class="referer" target="_blank">@return</a>
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws NumException
   */
  public String update(Object obj) throws IllegalArgumentException, IllegalAccessException, Exception {
    String sql = "update ";
    if (obj != null) {
      Field[] fiels = obj.getClass().getDeclaredFields();// 获得反射对象集合
      boolean t = obj.getClass().isAnnotationPresent(Table.class);// 获得类是否有注解
      if (t) {
        Table tab = obj.getClass().getAnnotation(Table.class);
        // sql += tab.name() + " set ";// 获得表名
        String wh = "";// 记录字段名
        String k = "";
        boolean bl = false;// 记录主键是否为空
        for (Field fl : fiels) {// 循环组装
          fl.setAccessible(true);// 开启支私有变量的访问权限
          Object tobj = fl.get(obj);
          if (tobj != null) {
            // if (fl.isAnnotationPresent(Key.class)) {// 判断是否存在主键
            // bl = true;
            // k = fl.getName() + "='" + tobj.toString() + "' where  ";
            // } else {
            // if (!fl.isAnnotationPresent(notRecord.class)) {
            // wh += fl.getName() + "='" + tobj.toString() + "',";
            // }
            // }
          }
        }
        if (bl) {
          if (wh.length() > 0)
            wh = wh.substring(0, wh.length() - 1);
          if (k.length() > 0)
            k = k.substring(0, k.length() - 1);
          sql += k + wh;
        } else
          throw new Exception("未找到类主键 主键不能为空");
      } else
        throw new Exception("传入对象不是实体类");
    } else
      throw new Exception("传入对象不能为空");// 抛出异常
    return sql;
  }

  private void setPreparedValues(PreparedStatement ps, Object... params) {

    try {
      Class type = params.getClass();
      if (type.isArray()) {
        Class elementType = type.getComponentType();
        System.out.println("Array of: " + elementType);
        System.out.println(" Length: " + Array.getLength(params));
      } else if((Object)params instanceof List ){
        System.out.println("11111");
      }

      if (params != null && params.length > 0) {
        for (int i = 0; i < params.length; i++) {
          ps.setObject(i + 1, params[i]);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    ParseSQLAnn ann = new ParseSQLAnn();

//    Object[] params = new Object[] {};
    List<String> aa = new ArrayList<String>();

    AtndSummarySheet sheet = new AtndSummarySheet();
    sheet.setStatdate(DateUtil.parseDateTime("2016-04-01"));
//    sheet.setEmpno("BI00014");
    // sheet.setEmpname("苏海华");
    sheet.setDeptname("财务部");

//    JdbcHandler jdbc = JdbcHandler.getInstance();
//    List<AtndSummarySheet> sheets = jdbc.findByAnnotatedSample(sheet);
//    System.out.println(sheets.size());
//    System.out.println(jdbc.saveAnnotatedBean(sheet));

  }

  /**
   * 通过实体类生成 insert into sql语句
   * 
   * @param annoBean
   *          <a href="http://my.oschina.net/u/556800" class="referer" target="_blank">@return</a>
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws NumException
   */
  public static String saveAnnotatedBean(Object annoBean) {
    String sql = "insert into ";
    if (annoBean != null) {
      if (annoBean.getClass().isAnnotationPresent(Table.class)) { // 获得类是否有注解
        Table tab = annoBean.getClass().getAnnotation(Table.class);
        sql += tab.name();// 获得表名
        String name = "";// 记录字段名
        String value = "";// 记录值名称
        boolean bl = false;// 记录主键是否为空
        Field[] fiels = annoBean.getClass().getDeclaredFields();// 获得反射对象集合
        for (Field fl : fiels) {// 循环组装
          fl.setAccessible(true);// 开启支私有变量的访问权限
          Object tobj = null;
          try {
            tobj = fl.get(annoBean);
            name += fl.getName() + ",";
            value += "'" + tobj.toString() + "',";
          } catch (IllegalArgumentException e) {
            e.printStackTrace();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
          if (tobj != null) {
            // if (fl.isAnnotationPresent(Key.class)) {// 判断是否存在主键
            // bl = true;
            // }
            // if (!fl.isAnnotationPresent(notRecord.class)) {
            // name += fl.getName() + ",";
            // value += "'" + tobj.toString() + "',";
            // }
          }
        }
        if (bl) {
          if (name.length() > 0)
            name = name.substring(0, name.length() - 1);
          if (value.length() > 0)
            value = value.substring(0, value.length() - 1);
          sql += "(" + name + ") values(" + value + ")";
        } else {
          // throw new Exception("未找到类主键 主键不能为空");
        }
      } else {
        // throw new Exception("传入对象不是实体类");
      }
    } else {
      // throw new Exception("传入对象不能为空");// 抛出异常

    }
    return sql;
  }

}