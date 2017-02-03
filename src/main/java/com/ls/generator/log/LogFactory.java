package com.ls.generator.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LogFactory {

	private static Map<String, FastLogger> instances = new HashMap<>();
	
	public static FastLogger getLogger(String name){
		FastLogger logger = instances.get(name);
		if(logger == null){
			logger = newInstance(name);
			if(logger != null){
				instances.put(name, logger);
			}
		}
		return logger;
	}
	
	private static FastLogger newInstance(String name){
		FastLogger logger = new FastLogger(name);
		return logger;
	}
	
	public static void close(){
		if(instances != null){
			Set<String> keys = instances.keySet();
			for (String key : keys) {
				try {
					instances.get(key).close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
