package com.ls.generator.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

	/**
	 * @param rs
	 * @param rowNum 下标从0开始
	 * @return
	 * @throws SQLException
	 */
	T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
