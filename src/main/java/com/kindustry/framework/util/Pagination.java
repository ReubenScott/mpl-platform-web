package com.kindustry.framework.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.kindustry.framework.jdbc.core.JdbcTemplate;



public class Pagination {
  
	private JdbcTemplate db;

	private int pageUtil;

	public Pagination(JdbcTemplate db, int pageUtil) {
		this.db = db;
		this.pageUtil = pageUtil;
	}

	String getSql(Connection conn, String tables, String cond, int curpage,
			String orderBy, String columns) {
		DatabaseMetaData dmd = null;
		StringBuffer sb = new StringBuffer();
		try {
			dmd = conn.getMetaData();
			String dbName = dmd.getDatabaseProductName();
			sb.append("select");
			if ("MySQL".equals(dbName)) {
				sb.append(columns);
				sb.append(" from ");
				sb.append(tables);
				sb.append(" where 1=1 ");
				sb.append(cond);
				sb.append(" order by ");
				sb.append(orderBy);
				sb.append(" limit ");
				sb.append((curpage - 1) * pageUtil);
				sb.append(", ");
				sb.append(pageUtil);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// public Page pagination(String columns,String cond,int curpage,String
	// orderBy,String tables,String id){
	// Page page=new Page();
	// Connection conn=null;
	// db=new BaseLogic().getDb();
	// try{
	// conn=db.getConnection();
	// String countSql="select count(*) from "+tables+" where 1=1 "+cond;
	// Object obj=db.queryForObject(conn, countSql, null);
	// int pageCount =Integer.parseInt(obj.toString());
	// page.setPageCount(pageCount);
	// int pageSize=pageCount%pageUtil>0? pageCount/pageUtil+1 :
	// pageCount/pageUtil;
	// page.setPageSize(pageSize);
	// String sql=getSql(conn,tables,cond,curpage,orderBy,columns);
	// List<Map> list=db.queryApp(conn, sql, null);
	// page.setDataList(list);
	// page.setPageUtil(pageUtil);
	// page.setCurpage(curpage);
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// db.close(conn);
	// }
	// return page;
	// }
	// public static void main(String [] args){
	// JdbcCore db=new
	// JdbcCore(Contants.DRIVER,Contants.URL,Contants.USER,Contants.PASSWORD);
	// Pagination p=new Pagination(db,2);
	// //Connection conn=db.getConnection();
	// Page page= p.pagination("*", "", 1, "deptID", "deptinfo", "deptID");
	// System.out.println(page.getCurpage()+" "+page.getPageCount()+"
	// "+page.getPageSize());
	// }
}
