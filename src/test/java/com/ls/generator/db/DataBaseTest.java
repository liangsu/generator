package com.ls.generator.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DataBaseTest {

	@Test
	public void testGetConn(){
		Connection conn = DataBase.getConn();
		Assert.assertTrue("获取数据库连接失败!", conn != null);
	}
	
	@Test
	public void testGetDSConn(){
		Connection conn = DataBase.getDSConn();
		Assert.assertTrue("从数据源获取数据库连接失败!", conn != null);
	}
	
	/**
	 * 测试获取连接用时
	 */
	@Test
	public void testUseTime(){
		for(int i = 0; i < 10; i++){
			long start = System.currentTimeMillis();
			Connection conn = DataBase.getDSConn();
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("从池中获取时间："+(System.currentTimeMillis() - start));
		}
		
		for(int i = 0;i < 10; i++){
			long start = System.currentTimeMillis();
			Connection conn = DataBase.getConn();
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("不从池中获取时间："+(System.currentTimeMillis() - start));
		}
	}
	
	@Test
	public void testExecuteQuery(){
		try {
			String sql = "select "
					   +     "ut.table_name,"
					   +     "utc.comments "
					   + "from user_tables ut "
					   + "left join user_tab_comments utc on ut.table_name = utc.table_name "
					   + "where ut.table_name = ? ";
			
			List<Table> tables = DataBase.executeQuery(sql, new Object[]{"USERS"}, new RowMapper<Table>() {
				@Override
				public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
					Table t = new Table();
					t.setTableName(rs.getString("table_name"));
					t.setComments(rs.getString("comments"));
					return t;
				}
			});
			
			Assert.assertTrue("获取USERS表信息失败!", tables != null && tables.size() > 0);
			for (Table table : tables) {
				System.out.println(table);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
