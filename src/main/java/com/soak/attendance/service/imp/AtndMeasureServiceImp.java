package com.soak.attendance.service.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.soak.attendance.service.AtndMeasureService;
import com.soak.framework.jdbc.DynamicDataSource;
import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.util.ExcelUtil;

public class AtndMeasureServiceImp implements AtndMeasureService{

  private JdbcHandler jdbc = JdbcHandler.getInstance();


  public static void main11(String args[]) {
    Workbook xwb = null;
    Connection connection = DynamicDataSource.getInstance().getSingleConnection();
    PreparedStatement ps = null;
    try {
      // 构造 XSSFWorkbook 对象，strPath 传入文件路径
      xwb = WorkbookFactory.create(new FileInputStream("E:/考勤/考勤_汇总.xlsx"));

      // 
      DatabaseMetaData dbmd = connection.getMetaData();
      ResultSet rs = dbmd.getColumns(null, null, "s_summary", null);
      // 字段类型
      List<Integer> columnTypes = new ArrayList<Integer>();
      while (rs != null && rs.next()) {
        columnTypes.add(rs.getInt("DATA_TYPE"));
      }

      // 根据表名 生成 Insert语句
      // "insert into CBOD_ECCMRAMR values (?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?,?,?,?)"
      StringBuffer sql;
      sql = new StringBuffer("insert into " + "s_summary" + " values (");
      for (int i = 1; i < columnTypes.size(); i++) {
        sql.append("?,");
      }
      sql.append("?)");

      ps = connection.prepareStatement(sql.toString());
      connection.setAutoCommit(false);
      int rowCount = 0;

      // 读取第一章表格内容

      for (int i = 6; i < xwb.getNumberOfSheets(); i++) {
        Sheet sheet = xwb.getSheetAt(i);
        String empname = sheet.getSheetName();

        // 循环输出表格中的内容
        // for (int i = sheet.getFirstRowNum(); i <
        // sheet.getPhysicalNumberOfRows(); i++) {
        Row row = sheet.getRow(34);

        List<String> cells = new ArrayList<String>();
        // 判断空行
        int cellnullcount = 0;
        for (int j = 7; j < row.getLastCellNum(); j++) {
          // 重新计数
          cellnullcount = 0;

          // 通过 row.getCell(j).toString() 获取单元格内容，
          Cell cell = row.getCell(j);

          String cellobjTmp;
          if (cell == null) {
            cellobjTmp = null;
            cellnullcount++;
          } else {
            cellobjTmp = ExcelUtil.convertCellToJava(cell);
          }
          cells.add(cellobjTmp);
        }

        // 判断 空行
        if (cellnullcount >= row.getPhysicalNumberOfCells()) {
        } else {
          rowCount++;
        }

        ps.setObject(1, empname);
        for (int index = 0; index < 12; index++) {
          ps.setObject(index + 2, castDBType(columnTypes.get(index), cells.get(index)));
        }

        ps.addBatch();

        if (rowCount % 1 == 0) {
          ps.executeBatch();
          connection.commit();
        }
        // break ;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
    }

  }

  /**
   * 根据数据库 字段类型 返回值
   * 
   * @param dbColumnType
   * @param value
   * @return
   */
  private static Object castDBType(int dbColumnType, String value) {
    if (value == null) {
      return null;
    }
    Object result = null;
    switch (dbColumnType) {
      case Types.DECIMAL:
        value = value.trim();
        result = new BigDecimal(value.equals("") ? "0" : value);
        break;
      case Types.BIT:
      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER:
      case Types.BIGINT:
        value = value.trim();
        if (value.equals("")) {
          result = 0;
        } else {
          double d = Double.valueOf(value.trim()).doubleValue();
          result = new DecimalFormat("#").format(d);
        }
        break;
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.LONGVARCHAR:
        result = value;
        break;
      case Types.DATE: // 2016-2-25 7:41:18
      case Types.TIMESTAMP: // 2016-2-25 7:41:18 时间戳
        value = value.trim();
        if (value.equals("")) {
          result = null;
        } else {
          result = Timestamp.valueOf(value);
        }
        break;
      case Types.TIME:
        // cell.setCellValue(rs.getTime(i + 1));
        break;
      case Types.DOUBLE:
      case Types.FLOAT:
      case Types.REAL:
        value = value.trim();
        if (value.equals("")) {
          result = 0;
        } else {
          result = Double.valueOf(value);
        }
        break;
    }

    return result;
  }

}