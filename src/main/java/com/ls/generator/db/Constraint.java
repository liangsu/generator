package com.ls.generator.db;

import java.io.Serializable;

public class Constraint implements Serializable{
	private String constraintName;
	private String tableName;
	private String columnName;
	private Integer position;
	
	public String getConstraintName() {
		return constraintName;
	}
	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		return "Constraint [constraintName=" + constraintName + ", tableName=" + tableName + ", columnName="
				+ columnName + ", position=" + position + "]";
	}
}
