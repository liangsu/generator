package com.ls.generator.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	private static DateFormat df = new SimpleDateFormat("yyyy年MM月dd");
	
	/**
	 * 生成带有时间戳的文件名称
	 * @param fileName
	 */
	public static String genNameWithDate(String fileName){
		if(StringUtils.isBlank(fileName)){
			throw new IllegalArgumentException("不能根据空文件名生成带有时间戳的文件名称");
		}
		
		int pos = fileName.lastIndexOf(".");
		String ext = "";
		String name = fileName;
		if(pos > -1){
			name = fileName.substring(0, pos);
			ext = fileName.substring(pos);
		}
		
		return name + df.format(new Date()) + ext;
	}
	
}
