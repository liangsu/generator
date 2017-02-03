package com.ls.generator.db;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ls.generator.db.Column;
import com.ls.generator.db.Table;
import com.ls.generator.db.TableManager;
import com.ls.generator.exception.DaoException;

public class TableManagerTest {

	TableManager tableManager = new TableManager();
	
	@Test
	public void testFindAllTables() {
		try {
			List<Table> tables = tableManager.findAllTables();
			for (Table table : tables) {
				System.out.println(table);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFindColumnsByTable() throws DaoException{
		List<Column> columns = tableManager.findColumnsByTable("CONTRACT");
		for (Column column : columns) {
			System.out.println(column);
		}
	}
	
}
