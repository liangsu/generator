package com.ls.generator;

import java.util.List;

import org.junit.Test;

import com.ls.generator.db.Table;
import com.ls.generator.exec.WriteReadTable;
import com.ls.generator.util.StringUtils;

public class StringUtilsTest {

	@Test
	public void testMatch(){
		String regex = "^[0-9a-zA-Z\\_-][0-9a-zA-Z\\_-]*[0-9a-zA-Z\\_-]$";
		List<Table> tables = WriteReadTable.readTableFromFile("tables2017年01月30.txt");
		for (int i = 0; i < tables.size(); i++) {
			if(!StringUtils.match(regex, tables.get(i).getTableName())){
				System.out.println(i + ":" + tables.get(i).getTableName());
			}
		}
	}
}
