package com.ls.generator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isBlank(String str){
		return (null == str || "".equals(str.trim()));
	}
	
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	
	public static boolean match(String regex, String str){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	
}
