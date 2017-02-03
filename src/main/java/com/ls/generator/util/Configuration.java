package com.ls.generator.util;

import java.io.IOException;
import java.util.Properties;

public class Configuration {

	private static Properties config = null;
	
	static{
		load();
	}
	
	private Configuration(){
		
	}
	
	private static void load(){
		config = new Properties();
		try {
			config.load(Configuration.class.getClassLoader().getResourceAsStream("generator.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key){
		System.out.println("获取配置："+key+":"+config.getProperty(key));
		return config.getProperty(key);
	}
	
	public static Integer getIntegerValue(String key){
		String val = getValue(key);
		return Integer.parseInt(val);
	}
	
}
