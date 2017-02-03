package com.ls.generator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ls.generator.db.Column;
import com.ls.generator.db.Table;
import com.ls.generator.db.TableManager;
import com.ls.generator.exception.DaoException;

public class TableSQLBuilderTest {

	@Test
	public void test1(){
		Table table = new Table();
		table.setTableName("PROVIDER_BIZ_MANS_SYN");
		table.setComments("商务联系人");
		
		List<Column> columns = new ArrayList<Column>();
		for(int i = 0; i < 10; i++){
			Column col = new Column();
			col.setColumnId(1);
			col.setColumnName("id");
			col.setComments("注解");
			col.setDataDefault(null);
			col.setDataLength(22);
			col.setDataType("NUMBER");
			col.setNullable(false);
			col.setTableName("PROVIDER_BIZ_MANS_SYN");
			columns.add(col);
		}
		table.setColumns(columns);
		
		TableSQLBuilder tsb = new TableSQLBuilder(table);
		System.out.println(tsb.getResult());
	}
	
	@Test
	public void test2() throws DaoException{
		TableManager tableManager = new TableManager();
		Table table = tableManager.findTableWithOthers("PROVIDER_BIZ_MANS_SYN");
		TableSQLBuilder tsb = new TableSQLBuilder(table);
		System.out.println(tsb.getResult());
	}
}
