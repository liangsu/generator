package com.ls.generator.html;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TableParser {

	private TableDirectoryRegister register;
	
	public TableParser(TableDirectoryRegister register) {
		this.register = register;
	}

	public void parseFile(String filePath) throws FileNotFoundException{
		String ext = filePath.substring(filePath.lastIndexOf(".")+1);
		
		FileInputStream is = new FileInputStream(filePath);
		
		if("pdm".equalsIgnoreCase(ext)){
			PdmParserDelegate pdmParserDelegate = new PdmParserDelegate(register);
			pdmParserDelegate.load(is);
			
		}else if("xml".equalsIgnoreCase(ext)){
			TableXmlParserDelegate tableXmlParserDelegate = new TableXmlParserDelegate(register);
			tableXmlParserDelegate.load(is);
			
		}
		
	}
}
