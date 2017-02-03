package com.ls.generator.exception;

public class DaoException extends Exception{

	private String tableName;
	
	public DaoException(String tableName, String message, Exception e) {
		super(message, e);
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Override
	public String getMessage() {
		String str = super.getMessage();;
		str += ":" + tableName;
		return str;
	}
}
