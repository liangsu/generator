package com.ls.generator.support;

/**
 * 解析数据库的数据类型
 * @author ls
 *
 */
public interface DataTypeResolver {

	/**
	 * 如果能够解析返回解析后的字符串，如果不能解析返回null
	 * @param dataType
	 * @return
	 */
	String resolve(DataType dataType);
	
}
