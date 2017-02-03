package com.ls.generator.db;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable{
	private String tableName;
	private String comments;
	private List<Column> columns;
	private List<Constraint> constraints;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	@Override
	public String toString() {
		return "Table [tableName=" + tableName + ", comments=" + comments + "]";
	}
	public List<Constraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}
	
}
