package com.ls.generator.exec;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.ls.generator.util.Configuration;

public class TableExecutor {

	public static void start() {
		//获取信息的线程池
		int nThread = Configuration.getIntegerValue("geterThreadNum");
		ExecutorService geterExecutorService = Executors.newFixedThreadPool(nThread, new ThreadFactory() {
			AtomicInteger counter = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("信息获取池-"+counter.getAndIncrement());
				return t;
			}
		});
		for(int i = 0; i < nThread; i++){
			geterExecutorService.execute(new TableOtherInfoGeter());
		}
		geterExecutorService.shutdown();
		
		//生成sql的处理池
		ExecutorService SQLGenExecutorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			AtomicInteger counter = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("sql生成池-"+counter.getAndIncrement());
				return t;
			}
		});
		SQLGenExecutorService.execute(new TableSQLGenerator());
		SQLGenExecutorService.shutdown();
	}
}
