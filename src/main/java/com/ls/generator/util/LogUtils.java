package com.ls.generator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogUtils {

	public static void appendToFile(String path, String text){
		if(path == null || "".equals(path)){
			path = FileUtils.genNameWithDate("db.sql");
		}
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fw = new FileWriter(file,true);
			bw = new BufferedWriter(fw);
			bw.append(text);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void logError(String path, Exception exception){
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			File file = new File(path);
			fw = new FileWriter(file,true);
			pw = new PrintWriter(fw);
			exception.printStackTrace(pw);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(pw != null){
				pw.close();
			}
			
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		appendToFile(null, "aaaaaaaaaaaa");
		appendToFile(null, "bbbbbbbbbb");
	}
}
