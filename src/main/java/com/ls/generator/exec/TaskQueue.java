package com.ls.generator.exec;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.ls.generator.db.Table;
import com.ls.generator.exception.DaoException;
import com.ls.generator.util.Configuration;

public class TaskQueue {

	private static ConcurrentLinkedQueue<Table> getTables = new ConcurrentLinkedQueue<Table>();
	
	private static AtomicInteger taskNum = null;
	
	private static ConcurrentLinkedQueue<Table> handleTables = new ConcurrentLinkedQueue<>();
	
	private static ConcurrentLinkedQueue<String> sqls = new ConcurrentLinkedQueue<>();
	
	private static ConcurrentLinkedQueue<Exception> exceptions = new ConcurrentLinkedQueue<>();
	
	static{
		String taskSource = Configuration.getValue("taskSource");
		String taskFile = Configuration.getValue("taskFile");
		if("db".equals(taskSource)){//从数据库获取任务来源
			try {
				taskFile = WriteReadTable.writeTableToFile();
			} catch (DaoException e) {
				e.printStackTrace();
			}
			List<Table> tabs = WriteReadTable.readTableFromFile(taskFile);
			if(tabs != null && tabs.size() > 0){
				taskNum = new AtomicInteger(tabs.size());
				for (Table tab : tabs) {
					getTables.offer(tab);
				}
			}
			
		}else if("file".equals(taskSource)){
			List<Table> tabs = WriteReadTable.readTableFromFile(taskFile);
			if(tabs != null && tabs.size() > 0){
				taskNum = new AtomicInteger(tabs.size());
				for (Table tab : tabs) {
					getTables.offer(tab);
				}
			}
			
		}else{
			List<Table> tabs = WriteReadTable.readTableWithOtherFromFile();
			if(tabs != null && tabs.size() > 0){
				taskNum = new AtomicInteger(tabs.size());
				for (Table tab : tabs) {
					handleTables.offer(tab);
				}
				synchronized (LockUtils.Table_sql_generate_Lock) {
					LockUtils.Table_sql_generate_Lock.notify();
				}
			}
		}
		System.out.println("任务总量："+taskNum);
	}
	
	public static boolean isHandleOver(){
		System.out.println("任务剩余量:"+taskNum.get());
		return taskNum.get() <= 0;
	}
	
	public static Table pollGetTable(){
		return getTables.poll();
	}
	
	/**
	 * 将指定元素插入此队列的尾部
	 * @param table
	 */
	public static void offerHandleTable(Table table){
		handleTables.offer(table);
		synchronized (LockUtils.Table_sql_generate_Lock) {
			LockUtils.Table_sql_generate_Lock.notify();
		}
	}
	
	/**
	 * 获取并移除此队列的头，如果此队列为空，则返回 null。
	 */
	public static Table pollHandleTable(){
		Table table = handleTables.poll();
		if(table != null){
			taskNum.decrementAndGet();
		}
		return table;
	}
	
	public static void offerSQL(String sql){
		sqls.offer(sql);
	}
	
	public static String pollSQL(){
		return sqls.poll();
	}
	
	public static void offerException(Exception e){
		exceptions.offer(e);
	}
	
	public static Exception pollException(){
		return exceptions.poll();
	}
}
