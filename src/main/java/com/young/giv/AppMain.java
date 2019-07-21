package com.young.giv;

import com.young.giv.output.MessageSender;
import com.young.giv.query.*;
import com.young.giv.utils.GivProperties;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class AppMain {
	
	public static void main(String[] args){
		if(args==null){
			System.out.println("args is null,exit");
			System.exit(-1);
		}
		String start = args[0];
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		if(start.equals("start")){
			MessageSender.getInstance().init();
			try{
//				QueryNcisrMember memberqquery = new QueryNcisrMember();
//				memberqquery.prepare();
				new WorkTimerMemberThread(null," Member").init();
				
				assembleView1();
				new WorkTimerThread(null," number 1", GivProperties.getFrequency1Time()).init();
				assembleView2();
				new WorkTimerThreadView(null," number 2", GivProperties.getFrequency2Time()).init();
			}catch(Exception e){
				System.out.println("start fail,"+e);
				System.exit(-1);
			}
		}		
   }
	

	public static List<Query> assembleView1(){
        List<Query> querys = new ArrayList<Query>();
    	
    	QueryStatsSum stats = new QueryStatsSum();
    	querys.add(stats);
    	
    	for (Query query : querys) {
    		try{
    			query.prepare();
    		}catch(Exception e){
    			System.out.println("prepare fail,"+query.getClass());
    			System.exit(-1);
    		}
		}
    	
    	return querys;
	}
	

    public static List<Query> assembleView2(){
    	
    	List<Query> querys = new ArrayList<Query>();
    	
//    	querys.add(new QueryCapital());
    	
    	querys.add(new QueryTradingVolume());
    	
    	querys.add(new QueryTimeout());
    	
//    	querys.add(new QueryTradingSucessRate());
    	
//    	querys.add(new QueryTradingTime());
    	
//    	querys.add(new QueryTradingTpm());
    	
//    	querys.add(new QueryTradingTps());
    	
    	for (Query query : querys) {
    		try{
    			query.prepare();
    		}catch(Exception e){
    			System.out.println("prepare fail,"+query.getClass());
    			System.exit(-1);
    		}
			
		}
    	
    	return querys;
	}
}