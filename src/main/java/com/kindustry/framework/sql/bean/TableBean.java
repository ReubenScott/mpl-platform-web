package com.kindustry.framework.sql.bean;

import java.util.ArrayList;
import java.util.List;

public class TableBean {

	private String domain;
	
	private String table;
	
	private List<ColumnBean> columnList;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		
		if (table.contains(".")){
			domain = table.split("\\.")[0];
			this.table = table.split("\\.")[1];
		}else{
			this.table = table;
		}
		
	}

	public List<ColumnBean> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnBean> columnList) {
		this.columnList = columnList;
	}
	
	/**得到insert的模板*/
	public String getInsertTemplate(){
		String insert = "insert into TABLE (COLUMN) values (PRETAG)";
		insert = insert.replaceFirst("TABLE", table);
		insert = insert.replaceFirst("COLUMN", getColumns());
		insert = insert.replaceFirst("PRETAG", getPretag("?"));
		
		return insert;
	}
	
	/**得到insert的模板，替换了参数*/
	public String getInsertTemplate(List<String> values){
		String insert = "insert into TABLE (COLUMN) values (PRETAG)";
		insert = insert.replaceFirst("TABLE", table);
		insert = insert.replaceFirst("COLUMN", getColumns());
		insert = insert.replaceFirst("PRETAG", getPretag(values,"'nodata'"));
		
		return insert;
	}
	
	private String getColumns (){
		
		StringBuffer buf = new StringBuffer();
		
		for (ColumnBean column : columnList){
			buf.append(column.getName()).append(",");
		}
		
		if (buf.length()>0){
			buf.deleteCharAt(buf.length()-1);
		}
		
		return buf.toString();
	}
	private String getPretag (String tag){
		return getPretag(new ArrayList<String>(), "?");
	}
	private String getPretag (List<String> tagList , String defaultValue){
		
		StringBuffer buf = new StringBuffer();
		for (int i = 0,j=columnList.size();i<j;i++){
			if (tagList.size() > i){
				buf.append("'").append(tagList.get(i)).append("'").append(",");
			}else{
				buf.append(defaultValue).append(",");
			}
		}
		
		if (buf.length()>0){
			buf.deleteCharAt(buf.length()-1);
		}
		
		return buf.toString();
	}
}
