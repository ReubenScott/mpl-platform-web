package com.soak.framework.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.framework.dao.IBasicDao;

/**
 * 
 */
public class BasicDaoImp extends BaseDaoImp implements IBasicDao {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

//  private JdbcTemplate jdbcTemplate;


  /**
   * 查询到处数据为Excel 2007文件
   * 
   * @param conn
   * @param filePath
   */
  public Workbook createExcelBySQl(String sql, Object... params) {
    Connection conn = jdbcTemplate.getConnection();
    PreparedStatement st = null;
    ResultSet rs = null;

    // 创建一个Excel 2007文件
    Workbook workbook = new SXSSFWorkbook();
    // 创建一个Excel的Sheet
    Sheet sheet = workbook.createSheet();
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

    try {
      st = conn.prepareStatement(sql);
      if (params != null) {
        for (int i = 0; i < params.length; i++) {
          st.setObject(i + 1, params[i]);
        }
      }
      rs = st.executeQuery();

      ResultSetMetaData rsmd = rs.getMetaData();
      // 数据字段名
      List<Integer> types = new ArrayList<Integer>();

      // 添加字段名 为Excel第一行数据
      Row sheetRow = sheet.createRow(0);
      for (int i = 1; i <= rsmd.getColumnCount(); i++) {
        String fieldName = rsmd.getColumnName(i);
        int type = rsmd.getColumnType(i);
        types.add(type);
        Cell cell = sheetRow.createCell(i - 1);
        // cell.setCellValue(new HSSFRichTextString(field));
        cell.setCellValue(fieldName);
      }

      // 开始 转行 添加数据
      int rowIndex = 1;
      while (rs.next()) {
        sheet.autoSizeColumn(rowIndex + 1, true);
        sheetRow = sheet.createRow(rowIndex++);
        List<Object> row = new ArrayList<Object>();
        for (int i = 0; i < types.size(); i++) {
          Cell cell = sheetRow.createCell(i);
          switch (types.get(i)) {
          case Types.DECIMAL:
            row.add(rs.getBigDecimal(i + 1));
            // cell.setCellValue(new HSSFRichTextString(rs.getBigDecimal(i+1).toString()));
            // cell.setCellStyle(cellStyle);
            cell.setCellValue(rs.getBigDecimal(i + 1).doubleValue());
            break;
          case Types.INTEGER:
            cell.setCellValue(rs.getInt(i + 1));
            break;
          case Types.BIGINT:
            cell.setCellValue(rs.getLong(i + 1));
            break;
          case Types.CHAR:
          case Types.VARCHAR:
          case Types.LONGVARCHAR:
            cell.setCellValue(rs.getString(i + 1));
            // cell.setCellValue(new HSSFRichTextString(rs.getString(i + 1)));
            break;
          case Types.DATE:
            cell.setCellValue(rs.getDate(i + 1));
            break;
          case Types.TIMESTAMP:
            cell.setCellValue(rs.getTimestamp(i + 1));
            break;
          case Types.TIME:
            cell.setCellValue(rs.getTime(i + 1));
            break;
          default:
            logger.error("type : " + types.get(i) + "  -  " + rs.getObject(i + 1));
          }
        }
      }
      rs.close();
      st.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (st != null) {
          st.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return workbook;
  }
  
  /**
   * 查询用户菜单
   */
  public List findUserMenus(String uid){
    String sql = "SELECT * FROM sys_menu ";
    
    return jdbcTemplate.queryForList(sql);
  }

  /**
   * @param module_id
   *          ģ���
   * @param station_id
   *          ��λ��
   * @param role_id
   *          Ȩ�޺�
   * @return �����Ƿ��и�ģ�������Ȩ��
   */
  public boolean havePermission(String module_id, String staff_id, String role_id) {
    String sql = "select count(*) cnt from station_role_module a,staff b where a.station_id=b.station_id and  a.module_id=? and b.staff_id=? and role_id=? ";
    ArrayList sqlList = new ArrayList();
    sqlList.add(module_id);
    sqlList.add(staff_id);
    sqlList.add(role_id);
    // HashMap data = jdbcTemplate.qryOneData(sql, sqlList);
    // int cnt = Integer.parseInt(data.get("cnt").toString());
    return false;
  }

  /**
   * ��ѯ�û�����Ȩ��
   */
  public boolean havePermission(String staff_id, int role_id) {
    String sql = "select 1  from station_role a, staff b, station c where a.station_id = b.station_id and a.station_id = c.station_id and c.state='S0A' and b.staff_id = ? and a.role_id=?";

    ArrayList paramList = new ArrayList();
    paramList.add(staff_id);
    paramList.add(new Integer(role_id));

    return true;
  }

  public HashMap[] getUnitList() {
    String sql = "select area_code code,unit_name display from unit where unit_id<>0";
    return null;
  }

}
