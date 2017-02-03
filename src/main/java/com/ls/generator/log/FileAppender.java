package com.ls.generator.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class FileAppender {

	FileWriter fw = null;
	
	public FileAppender(String fileName) {
		File file = new File(fileName);
		try {
			fw = new FileWriter(file, true);
		} catch (IOException e) {
			System.err.println("初始化日志错误！");
			e.printStackTrace();
		}
	}

	public void appendThrowable(Throwable t){
		//打印异常信息
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw); 
		t.printStackTrace(pw);
		StringBuffer sb = sw.getBuffer();
		doAppend(sb.toString());
	}
	
	public void doAppend(String message){
		try {
			fw.write(message);
			fw.write("\n");
			//fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close(){
		if(fw != null){
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fw = null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
	}
	
}
