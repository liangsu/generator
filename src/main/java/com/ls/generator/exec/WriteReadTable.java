package com.ls.generator.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.ls.generator.db.Table;
import com.ls.generator.db.TableManager;
import com.ls.generator.exception.DaoException;
import com.ls.generator.util.FileUtils;
import com.ls.generator.util.StringUtils;

public class WriteReadTable {

	private static final String TABLE_FILE_SPLIT = ":";
	
	public static String writeTableToFile() throws DaoException {
		TableManager tableManager = new TableManager();
		
		long start = System.currentTimeMillis();
		List<Table> tables = tableManager.findAllTables();
		System.out.println(tables.size());
		System.out.println("从数据库读取用时："+(System.currentTimeMillis() - start)+"ms");
		
		start = System.currentTimeMillis();
		String fileName = FileUtils.genNameWithDate("tables.txt");
		File file = new File(fileName);
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (Table table : tables) {
				if(StringUtils.isBlank(table.getComments())){
					bw.write(table.getTableName()+"\n");
				}else{
					bw.write(table.getTableName() + TABLE_FILE_SPLIT + table.getComments().trim()+"\n");
				}
			}
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
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
			System.out.println("写文件用时："+(System.currentTimeMillis() - start)+"ms");
		}
		return fileName;
	}
	
	public static List<Table> readTableFromFile(String fileName){
		long start = System.currentTimeMillis();
		
		List<Table> tables = new ArrayList<>();
		
		File file = new File(fileName);
		
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			String line = br.readLine();
			while(line != null){
				Table table = new Table();
				int pos = line.indexOf(TABLE_FILE_SPLIT);
				if(pos > -1){
					String[] tableComments = line.split(":");
					table.setTableName(tableComments[0].trim());
					table.setComments(tableComments[1].trim());
				}else{
					table.setTableName(line.trim());
				}
				tables.add(table);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fr != null){
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("读取任务文件用时："+(System.currentTimeMillis() - start)+"ms");
		return tables;
	}
	
	/**
	 * 将具有完整信息的table以二进制的形式写入文件
	 * @param tables
	 */
	public static void writeTableWithOtherToFile(List<Table> tables){
		OutputStream os = null;
		try {
			os = new FileOutputStream("table.dat");
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(tables);
			oos.flush();
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 读取带有全信息的表信息
	 * @return
	 */
	public static List<Table> readTableWithOtherFromFile(){
		List<Table> tables = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("table.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			tables = (List<Table>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tables;
	}

}