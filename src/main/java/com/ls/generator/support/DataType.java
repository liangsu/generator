package com.ls.generator.support;

public interface DataType {

	/**
	 * 获取数据类型
	 * @return
	 */
	String getDataType();
	
	Integer getDataLength();
	
	Integer getDataPrecision();
	
	Integer getDataScale();
}
