package com.ls.generator.exec;

import com.ls.generator.db.Table;
import com.ls.generator.db.TableManager;
import com.ls.generator.exception.DaoException;
import com.ls.generator.log.FastLogger;
import com.ls.generator.log.LogFactory;

public class TableOtherInfoGeter implements Runnable{

	FastLogger log = LogFactory.getLogger("log.txt");
	FastLogger errTabLog = LogFactory.getLogger("errorTable.txt");
	
	@Override
	public void run() {
		TableManager tableManager = new TableManager();
		
		while(true){
			long start = System.currentTimeMillis();
			Table table = TaskQueue.pollGetTable();
			if(table == null){
				break;
			}
			
			try {
				table = tableManager.findTableWithOthers(table);
				log.debug("获取了一张表的全部信息:"+table.getTableName());
			} catch (DaoException e) {
				log.error(e.getMessage(), e);
				errTabLog.error(e.getTableName());
			}
			
			TaskQueue.offerHandleTable(table);
			log.debug(Thread.currentThread().getName()+":获取一个table用时："+(System.currentTimeMillis() - start)+"ms");
			//System.out.println(Thread.currentThread().getName()+":获取一个table用时："+(System.currentTimeMillis() - start)+"ms");
		}
		
		log.debug(Thread.currentThread().getName()+":over");
		//System.out.println(Thread.currentThread().getName()+":over");
	}


}
