package com.ls.generator.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ls.generator.exception.DaoException;
import com.ls.generator.util.LogUtils;
import com.ls.generator.util.StringUtils;

public class TableManager{

	private RowMapper<Table> tableMapper = new RowMapper<Table>() {
		@Override
		public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
			Table t = new Table();
			t.setTableName(rs.getString("table_name"));
			t.setComments(rs.getString("comments"));
			return t;
		}
	};
	
	private RowMapper<Column> columnMapper = new RowMapper<Column>() {
		@Override
		public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
			Column column = new Column();
			column.setTableName(rs.getString("table_name"));
			column.setColumnId(getIntegerFromRs(rs, "COLUMN_ID"));
			column.setColumnName(rs.getString("column_name"));
			column.setDataType(rs.getString("data_type"));
			column.setDataLength(getIntegerFromRs(rs, "DATA_LENGTH"));
			column.setDataPrecision(getIntegerFromRs(rs, "DATA_PRECISION"));
			column.setDataScale(getIntegerFromRs(rs, "DATA_SCALE"));
			String nullable = rs.getString("NULLABLE");
			if(nullable != null && "Y".equals(nullable)){
				column.setNullable(true);
			}else{
				column.setNullable(false);
			}
			column.setDataDefault(rs.getString("DATA_DEFAULT"));
			column.setComments(rs.getString("comments"));
			return column;
		}
	};
	
	private RowMapper<Constraint> constraintMapper = new RowMapper<Constraint>() {
		@Override
		public Constraint mapRow(ResultSet rs, int rowNum) throws SQLException {
			Constraint constraint = new Constraint();
			constraint.setConstraintName(rs.getString("constraint_name"));
			constraint.setTableName(rs.getString("table_name"));
			constraint.setColumnName(rs.getString("column_name"));
			constraint.setPosition(getIntegerFromRs(rs, "position"));
			return constraint;
		}
	};
	
	public List<Table> findAllTables() throws DaoException{
		try {
			String sql = "select "
					   +     "ut.table_name,"
					   +     "utc.comments "
					   + "from user_tables ut "
					   + "left join user_tab_comments utc on ut.table_name = utc.table_name "
					   + "order by ut.table_name";
			
			return DataBase.executeQuery(sql, null, tableMapper);
		} catch (SQLException e) {
			throw new DaoException(null, "获取所有的表信息发生错误", e);
		}
	}
	
	
	public Table findTableWithOthers(Table table) throws DaoException{
		if(table == null || StringUtils.isBlank(table.getTableName())){
			return null;
		}
		table.setColumns(findColumnsByTable(table.getTableName()));
		table.setConstraints(findConstraintsByTable(table.getTableName()));
		return table;
	}
	
	public Table findTableWithOthers(String tableName) throws DaoException{
		try {
			String sql = "select "
					   +     "ut.table_name,"
					   +     "utc.comments "
					   + "from user_tables ut "
					   + "left join user_tab_comments utc on ut.table_name = utc.table_name "
					   + "where ut.table_name = ? ";
			
			 return DataBase.getSingleResult(sql, new Object[]{tableName}, tableMapper);
		} catch (SQLException e) {
			throw new DaoException(tableName, "获取所有的表信息发生错误", e);
		}
	}
	
	public List<Column> findColumnsByTable(String tableName) throws DaoException{
		try {
			String sql = "select utc.TABLE_NAME, "
                       +     "utc.COLUMN_ID, "
                       +     "utc.COLUMN_NAME, "
                       +     "utc.DATA_TYPE, "
                       +     "utc.DATA_LENGTH, "
                       +     "utc.DATA_PRECISION, "
                       +     "utc.DATA_SCALE, "
                       +     "utc.NULLABLE, "
                       +     "utc.DATA_DEFAULT, "
                       +     "ucc.comments "
                       + "from user_tab_columns utc, user_col_comments ucc "
                       + "where utc.TABLE_NAME = ucc.table_name "
                       +     "and utc.COLUMN_NAME = ucc.column_name "
                       +     "and utc.table_name = ? "
                       + "order by utc.COLUMN_ID"; 
			
			return DataBase.executeQuery(sql, new Object[]{tableName}, columnMapper);
		} catch (SQLException e) {
			LogUtils.logError("daoError.txt", new Exception(tableName,e));
			LogUtils.appendToFile("daoErrorTable.txt", tableName+"\n");
			throw new DaoException(tableName, "获取所有的表的列发生错误", e);
		}
	}
	
	public List<Constraint> findConstraintsByTable(String tableName) throws DaoException{
		try {
			String sql = "select "
                       +     "constraint_name, "
                       +     "table_name, "
                       +     "column_name, "
                       +     "position "
                       + "from user_cons_columns " 
                       + "where table_name = ? and constraint_name = "
                       +     "(select constraint_name from user_constraints where table_name = ? and constraint_type ='P') "
                       + "order by position asc"; 
			
			return DataBase.executeQuery(sql, new Object[]{tableName, tableName}, constraintMapper);
		} catch (SQLException e) {
			LogUtils.logError("daoError.txt", new Exception(tableName,e));
			LogUtils.appendToFile("daoErrorTable.txt", tableName+"\n");
			throw new DaoException(tableName, "获取所有的表的列发生错误", e);
		}
	}
	
	private Integer getIntegerFromRs(ResultSet rs, String columnName) throws SQLException{
		return DataBase.getIntegerFromRs(rs, columnName);
	}
}
