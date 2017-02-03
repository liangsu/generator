package com.ls.generator.exec;

import java.util.ArrayList;
import java.util.List;

import com.ls.generator.TableSQLBuilder;
import com.ls.generator.db.Table;
import com.ls.generator.log.FastLogger;
import com.ls.generator.log.LogFactory;
import com.ls.generator.util.StringUtils;

public class TableSQLGenerator implements Runnable{

	FastLogger log = LogFactory.getLogger("log.txt");
	FastLogger sqlLog = LogFactory.getLogger("db.sql");
	FastLogger errTabLog = LogFactory.getLogger("errorTable.txt");
	
	private List<Table> handedTables = new ArrayList<>();
	
	@Override
	public void run() {
		
		int taskNum = 0;
		while(true){
				Table table = TaskQueue.pollHandleTable();
				if(table == null){
					if(TaskQueue.isHandleOver() ){//如果获取任务已经完成，则停止该线程
						System.out.println("break");
						log.debug("sql生成器生成完了所有表!");
						break;
					}else{
						synchronized (LockUtils.Table_sql_generate_Lock) {
							try {
								System.out.println("wait...");
								LockUtils.Table_sql_generate_Lock.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				if(table == null){
					//System.out.println("continue");
					continue;
				}
				
				try {
					long start = System.currentTimeMillis();
					TableSQLBuilder tsb = new TableSQLBuilder(table);
					String sql = tsb.getResult();
					TaskQueue.offerSQL(sql);
					log.debug(Thread.currentThread().getName()+":生成table:["+table.getTableName()+"]的SQL用时："+(System.currentTimeMillis() - start)+"ms");
					//System.out.println(Thread.currentThread().getName()+":生成一个table的SQL用时："+(System.currentTimeMillis() - start)+"ms");
					//System.out.println(sql);
					
					start = System.currentTimeMillis();
					sqlLog.debug(sql);
					//LogUtils.appendToFile(null, sql);
					log.debug(Thread.currentThread().getName()+":向文件中写入一个table:["+table.getTableName()+"]用时："+(System.currentTimeMillis() - start)+"ms");
					//System.out.println(Thread.currentThread().getName()+":向文件中写入一个table用时："+(System.currentTimeMillis() - start)+"ms");
					handedTables.add(table);
					System.out.println("生成了table["+table.getTableName()+"]的sql,处理任务量:"+(++taskNum));
				} catch (Exception e) {
					e.printStackTrace();
					TaskQueue.offerException(e);
					//LogUtils.logError("error.txt", e);
					log.error("生成表"+table.getTableName()+"的sql出错", e);
					
					if(StringUtils.isBlank(table.getComments())){
						errTabLog.debug(table.getTableName());
						//LogUtils.appendToFile("errorTable.txt", table.getTableName()+"\n");
					}else{
						errTabLog.debug(table.getTableName()+":"+table.getComments());
						//LogUtils.appendToFile("errorTable.txt", table.getTableName()+":"+table.getComments()+"\n");
					}
				}
			}
		log.debug(Thread.currentThread().getName()+":sql生成器处理完所有任务...");
		System.out.println(Thread.currentThread().getName()+":sql生成器处理完所有任务...");
		
		//将具有完整信息的table以二进制的形式写入文件
		WriteReadTable.writeTableWithOtherToFile(handedTables);
		
		//关闭所有的日志记录器
		LogFactory.close();
	}

}
