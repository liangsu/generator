package com.ls.generator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.ls.generator.util.Configuration;

public class DataBase {

	private static DataSource ds = null;
	
	static{
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(Configuration.getValue("jdbc.driverClass"         ));
		bds.setUrl            (Configuration.getValue("jdbc.url"                 ));
		bds.setUsername       (Configuration.getValue("jdbc.user"                ));
		bds.setPassword       (Configuration.getValue("jdbc.password"            ));
		bds.setInitialSize    (Configuration.getIntegerValue("ds.initialSize"    ));
		bds.setMaxActive      (Configuration.getIntegerValue("ds.maxActive"      ));
		bds.setMinIdle        (Configuration.getIntegerValue("ds.minIdle"        ));
		bds.setMaxIdle        (Configuration.getIntegerValue("ds.maxIdle"        ));
		bds.setMaxWait        (Configuration.getIntegerValue("ds.maxWait"        ));
		bds.setValidationQuery(Configuration.getValue       ("ds.validationQuery"));
		//bds.setRemoveAbandoned(true);
		//bds.setRemoveAbandonedTimeout(10);
		ds = bds;
	}
	
	public static Connection getDSConn(){
		if(ds == null){
			throw new RuntimeException("连接池未初始化!");
		}
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("从连接池获取连接失败!", e);
		}
		
		if(conn == null){
			throw new RuntimeException("从连接池获取的连接是空的!");
		}

		return conn;
	}
	
	public static Connection getConn(){
		String driver   = Configuration.getValue("jdbc.driverClass");
		String url      = Configuration.getValue("jdbc.url"        );
		String user     = Configuration.getValue("jdbc.user"       );
		String password = Configuration.getValue("jdbc.password"   );
		Connection conn = null;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(pstmt != null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Integer getIntegerFromRs(ResultSet rs, String columnName) throws SQLException{
		Object obj = rs.getObject(columnName);
		if(obj != null){
			return Integer.parseInt(obj.toString());
		}
		return null;
	}
	
	public static <T> List<T> executeQuery(String sql, Object[] params, RowMapper<T> mapper) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<T> list = new ArrayList<T>();
		try {
			conn = getDSConn();
			ps = conn.prepareStatement(sql);
			
			//设置参数
			initParameters(ps, params);

			//获取结果集
			rs = ps.executeQuery();
			
			int rowNum = 0;
			while(rs.next()){
				T t = mapper.mapRow(rs, rowNum);
				rowNum++;
				list.add(t);
			}
		} catch (SQLException e) {
			throw e;
		}finally {
			close(conn, ps, rs);
		}
		return list;
	}
	
	public static <T> T getSingleResult(String sql, Object[] params, RowMapper<T> mapper) throws SQLException{
		List<T> list = executeQuery(sql, params, mapper);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public static int executeUpdate(String sql, Object... params) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;
		try {
			conn = getDSConn();
			ps = conn.prepareStatement(sql);
			
			//设置参数
			initParameters(ps, params);
			
			//执行
			count = ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}finally {
			close(conn, ps, null);
		}
		return count;
	}
	
	private static void initParameters(PreparedStatement ps, Object... params) throws SQLException{
		//设置参数
		if(params != null && params.length > 0){
			for(int i = 0; i < params.length; i++){
				ps.setObject(i+1, params[i]);
			}
		}
	}
	
}
