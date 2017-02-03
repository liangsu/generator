package com.ls.generator.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractRowMapper<T> implements RowMapper<T>{

	public static Integer getIntegerFromRs(ResultSet rs, String columnName) throws SQLException{
		return DataBase.getIntegerFromRs(rs, columnName);
	}
	
}
