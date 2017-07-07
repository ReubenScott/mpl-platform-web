package com.kindustry.framework.sql.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kindustry.framework.sql.bean.ColumnBean;
import com.kindustry.framework.sql.bean.TableBean;
import com.kindustry.framework.sql.util.StringUtil;

/**
 * 单表解析
 * */
public class TableAnalysis {

	private static Log log = LogFactory.getLog(TableAnalysis.class);
	
	private String sql;
	
	private TableBean bean;
	
	private List<ColumnBean> columnList;
	
	private List<String> keywordList = Arrays.asList(new String[]{"primary"});
	
	
	public TableAnalysis(String sql) {
		this.sql =sql.toLowerCase();
		bean = new TableBean();
		columnList = new ArrayList<ColumnBean>();
	}
	
	public TableBean generate(){
		parseTableName();
		parseColumn();
		bean.setColumnList(columnList);
		return bean;
	}
	
	private void parseTableName(){
		String tableName = StringUtil.find(sql, "table", "(");
		bean.setTable(tableName);
	}
	
	private void parseColumn(){
		//此正则表达式只支持逗号前有空格的分割
		String columnSql = StringUtil.findMaxRange(sql, "(", ")");
		String[] column = columnSql.split("\\s+,");
		for (String c : column){
			if (checkColumnName(c.trim().toLowerCase())){
				columnList.add(new ColumnBean(c.trim()));
			}
		}
	}
	/**检查列名是否合法*/
	private boolean checkColumnName(String name){
		for (String keyword : keywordList){
			if (name.startsWith(keyword)){
				return false;
			}
		}
		return true;
	}
}
