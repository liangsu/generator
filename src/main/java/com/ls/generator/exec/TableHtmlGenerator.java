package com.ls.generator.exec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ls.generator.db.Table;
import com.ls.generator.html.TableDirectoryRegister;
import com.ls.generator.html.TableParser;
import com.ls.generator.util.StringUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class TableHtmlGenerator {

	public static void generator(List<Table> tables){
		
		TableDirectoryRegister register = new TableDirectoryRegister();
		TableParser parser = new TableParser(register);
		try {
			//parser.parseFile("E:\\Workspaces\\workspace_p6spy\\generator\\src\\main\\java\\table2Html.xml");
			parser.parseFile("C:\\Users\\warhorse\\Desktop\\采购辅助系统-数据模型汇总V2.0.PDM");
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		List<String> dirs = register.getDirs();
		for(int i = 0; dirs != null && i < dirs.size(); i++){
			String dir = dirs.get(i);
			File file = new File(dir);
			if(file.exists()){
				if(!file.isDirectory()){
					file.mkdirs();
				}
			}else{
				file.mkdirs();
			}
		}
		
		Configuration configuration = new Configuration(freemarker.template.Configuration.VERSION_2_3_23);
		configuration.setClassForTemplateLoading(TableHtmlGenerator.class.getClassLoader().getClass(),"/");
		configuration.setDefaultEncoding("utf-8");
		configuration.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
		try {
			Template template = configuration.getTemplate("table.html");
			
			for(int i = 0; tables != null && i < tables.size(); i++){
				Table table = tables.get(i);
				try {
					String dir = register.getTableDir(table.getTableName());
					if(StringUtils.isNotBlank(dir)){
							Map<String, Object> dataModel = new HashMap<String, Object>();
							dataModel.put("table", table);
							dataModel.put("TableUtils", "com.ls.generator.util.TableUtils");
							FileOutputStream fos = new FileOutputStream( dir + File.separator + table.getTableName()+".html");
							Writer writer = new PrintWriter(fos);
							template.process(dataModel, writer);
							writer.close();
							System.out.println("生成表"+table.getTableName()+"的html成功!");
					}
				} catch (FileNotFoundException e) {
					System.out.println("生成表失败："+table.getTableName());
					e.printStackTrace();
				} catch (TemplateException e) {
					System.out.println("生成表失败："+table.getTableName());
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("生成表失败："+table.getTableName());
					e.printStackTrace();
				}
			}// end of for(...)
			
		} catch (TemplateNotFoundException e1) {
			e1.printStackTrace();
		} catch (MalformedTemplateNameException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
