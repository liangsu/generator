package com.ls.generator.db;

import java.io.Serializable;

import com.ls.generator.support.DataType;

public class Column implements DataType, Serializable{
	private String tableName;
	private Integer columnId;
	private String columnName;
	private String dataType;
	private Integer dataLength;//当前列的字节长度
	private Integer dataPrecision;//表示字段类型的精度的总长度，如果为null,表示精度的总长度不固定，最长为Data_Length
	private Integer dataScale;//表示字段类型的精度范围，如果为0,表示只能存储为整数,如果为null,表示可以存储整数或者浮点数，浮点数位数不确定，如果为整数，表示存储的精度位数。
	private boolean nullable;
	private String dataDefault;
	private String comments;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getColumnId() {
		return columnId;
	}
	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getDataLength() {
		return dataLength;
	}
	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}
	public Integer getDataPrecision() {
		return dataPrecision;
	}
	public void setDataPrecision(Integer dataPrecision) {
		this.dataPrecision = dataPrecision;
	}
	public Integer getDataScale() {
		return dataScale;
	}
	public void setDataScale(Integer dataScale) {
		this.dataScale = dataScale;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public String getDataDefault() {
		return dataDefault;
	}
	public void setDataDefault(String dataDefault) {
		this.dataDefault = dataDefault;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "Column [tableName=" + tableName + ", columnId=" + columnId + ", columnName=" + columnName
				+ ", dataType=" + dataType + ", dataLength=" + dataLength + ", dataPrecision=" + dataPrecision
				+ ", dataScale=" + dataScale + ", nullable=" + nullable + ", dataDefault=" + dataDefault + ", comments="
				+ comments + "]";
	}
}
