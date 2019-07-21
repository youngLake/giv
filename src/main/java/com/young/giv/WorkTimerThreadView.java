package com.young.giv;

import com.young.giv.query.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WorkTimerThreadView implements Runnable {
	 private Log log = LogFactory.getLog(WorkTimerThreadView.class);
	 private String name;
	 private List<Query>  querys;
	 private int frequencyTime = 20;
	 
	 private ScheduledFuture<?> future;
	 
	 public WorkTimerThreadView(List<Query>  querys, String name, int frequencyTime){
		 this.querys = querys;
		 this.name = name;
		 this.frequencyTime = frequencyTime;
	 }
	 
	 
	 public WorkTimerThreadView(List<Query>  querys, String name){
		 this.querys = querys;
		 this.name = name;
	 }
	 
	 public void init(){
	     future= WorkThreadPool.getInstance().getSchedulePool()
	                .scheduleWithFixedDelay(this,
	                        180, frequencyTime, TimeUnit.SECONDS);
	 }


	public void run() {
		log.info("query queue data name{}"+name+",start frequecy query...");
		List<Query> newquerys = assembleView2();
		if(newquerys!= null){
			for(Query query:newquerys){
				try{
				  WorkThreadPool.getInstance().execute(new ExecThread(query));
				}catch(Throwable e){
					log.error("start exec query fail!",e);
				}
			}
		}
	}
	
    public static List<Query> assembleView2(){
    	
    	List<Query> querys = new ArrayList<Query>();
    	
//    	querys.add(new QueryCapital());
    	
    	querys.add(new QueryTradingVolume());
    	
    	querys.add(new QueryTimeout());
    	
//    	querys.add(new QueryTradingSucessRate());
//
//    	querys.add(new QueryTradingTime());
//
//    	querys.add(new QueryTradingTpm());
//
//    	querys.add(new QueryTradingTps());
    	
    	return querys;
	}
	
	
	public void stop(){
		if (future != null) {
            future.cancel(true);
        }
	}
}
