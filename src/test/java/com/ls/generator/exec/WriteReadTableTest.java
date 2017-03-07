package com.ls.generator.exec;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ls.generator.db.Column;
import com.ls.generator.db.Constraint;
import com.ls.generator.db.Table;
import com.ls.generator.exception.DaoException;

public class WriteReadTableTest {

	@Test
	public void testWriteTableToFile(){
		try {
			WriteReadTable.writeTableToFile();
		} catch (DaoException e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadTableFromFile(){
		List<Table> tables = WriteReadTable.readTableWithOtherFromFile();
		for (Table table : tables) {
			if("USERS".equals(table.getTableName())){
				System.out.println(table);
				List<Column> cols = table.getColumns();
				for (Column column : cols) {
					System.out.println(column);
				}
				
				List<Constraint> cons = table.getConstraints();
				for (Constraint con : cons) {
					System.out.println(con);
				}
			}
		}
	}
	
	/**
	 * 生成数据库表的html页面
	 */
	@Test
	public void testGeneraterTableHtml(){
		List<Table> tables = WriteReadTable.readTableWithOtherFromFile();
		for (Table table : tables) {
			TableHtmlGenerator.generator(Arrays.asList(table));
		}
	}
	
	/**
	 * 生成数据库表的html页面
	 */
	@Test
	public void testGeneraterTableHtml2(){
		List<Table> tables = WriteReadTable.readTableWithOtherFromFile();
		TableHtmlGenerator.generator(tables);
	}
}
