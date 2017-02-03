package com.ls.generator.log;

import com.ls.generator.util.Configuration;
import com.ls.generator.util.FileUtils;

public class FastLogger{

	private boolean isDebugEnable;
	private FileAppender appender  = null;
	private boolean close = false;
	
	public FastLogger(String fileName){
		String newFileName = FileUtils.genNameWithDate(fileName);
		appender = new FileAppender(newFileName);
		
		String level = Configuration.getValue("log.level");
		if("error".equalsIgnoreCase(level)){
			isDebugEnable = false;
		}else{
			isDebugEnable = true;
		}
	}
	
	public void debug(Object message){
		if(isDebugEnable){
			appender.doAppend(String.valueOf(message));
		}
	}
	
	public void debug(Object message, Throwable t){
		if(isDebugEnable){
			appender.doAppend(String.valueOf(message));
			appender.appendThrowable(t);
		}
	}
	
	public void error(Object message){
		appender.doAppend(String.valueOf(message));
	}
	
	public void error(Object message, Throwable t){
		appender.doAppend(String.valueOf(message));
		appender.appendThrowable(t);
	}
	
	public boolean isDebugEnable(){
		return isDebugEnable;
	}
	
	public void close(){
		if(close){
			return ;
		}
		appender.close();
	}

}
