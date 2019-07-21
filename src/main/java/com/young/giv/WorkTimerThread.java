package com.young.giv;

import com.young.giv.query.Query;
import com.young.giv.query.QueryStatsSum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WorkTimerThread implements Runnable {
	 private Log log = LogFactory.getLog(WorkTimerThread.class);
	 private String name;
	 private List<Query>  querys;
	 private int frequencyTime = 20;
	 
	 private ScheduledFuture<?> future;
	 
	 public WorkTimerThread(List<Query>  querys, String name, int frequencyTime){
		 this.querys = querys;
		 this.name = name;
		 this.frequencyTime = frequencyTime;
	 }
	 
	 
	 public WorkTimerThread(List<Query>  querys, String name){
		 this.querys = querys;
		 this.name = name;
	 }
	 
	 public void init(){
	     future= WorkThreadPool.getInstance().getSchedulePool()
	                .scheduleWithFixedDelay(this,
	                        120, frequencyTime, TimeUnit.SECONDS);
	 }


	public void run() {
		log.info("query queue data name{}"+name+",start frequecy query...");
//		if(querys!= null){
			QueryStatsSum stats = new QueryStatsSum();
			try{
				WorkThreadPool.getInstance().execute(new ExecThread(stats));
			}catch(Throwable e){
			    log.error("start exec query fail!",e);
			}
//			for(Query query:querys){
//				try{
//				  WorkThreadPool.getInstance().execute(new ExecThread(query));
//				}catch(Exception e){
//					log.error("start exec query fail!",e);
//				}
//			}
//		}
	}
	
	
	public void stop(){
		if (future != null) {
            future.cancel(true);
        }
	}
}
