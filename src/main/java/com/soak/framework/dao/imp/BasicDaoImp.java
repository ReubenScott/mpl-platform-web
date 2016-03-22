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
