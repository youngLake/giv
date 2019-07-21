package com.young.giv;

import com.young.giv.query.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExecThread implements Runnable{
	private static Log log = LogFactory.getLog(ExecThread.class);
	
	private Query query;
	
	public ExecThread(Query query){
		this.query = query;
	}
	

	public void run() {
		if(query == null){
			 log.error("query is not null");
	         System.exit(1);
		}
		query.emit();
	}

}
