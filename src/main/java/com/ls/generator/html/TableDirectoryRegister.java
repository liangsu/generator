package com.ls.generator.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableDirectoryRegister {

	private Map<String, String> tableDire = new HashMap<>();
	private List<String> dirs = new ArrayList<String>();
	
	public void registe(String tableName, String directory){
		dirs.add(directory);
		tableDire.put(tableName.trim().toUpperCase(), directory);
	}
	
	public List<String> getDirs(){
		return dirs;
	}
	
	public String getTableDir(String tableName){
		return tableDire.get(tableName.trim().toUpperCase());
	}
}
