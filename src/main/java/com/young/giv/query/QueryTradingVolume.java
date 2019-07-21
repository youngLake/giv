package com.young.giv.query;

import com.young.giv.httpClient.HttpDataFilter;
import com.young.giv.utils.DataParseUtil;
import com.young.giv.utils.DatetimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class QueryTradingVolume extends Query {
	private static Log log = LogFactory.getLog(QueryTradingVolume.class);
	private static boolean initFlag = false;
	private static long prevTime;

	@Override
	public void prepare() {
		prevTime = DatetimeUtil.getCurrentDayPrecision();
		new Thread(new TradingVolumeThead()).start();
	}

	@Override
	public void emit() {
		if(initFlag){
			long endTime = DatetimeUtil.getCurrentDate();
			long startTime = prevTime;
			dealData(startTime,endTime);
		}
		
	}	
	
	public void dealData(long startTime,long endTime){

	}

	@Override
	public void release() {

	}
	
	public class TradingVolumeThead implements Runnable{
		private static final long devTime = 60 * 60 * 1000; //һ��Сʱ����
		
		public TradingVolumeThead(){
			
		}

		public void run() {
			long startDate = DatetimeUtil.getCurrentDate();
			while(startDate - prevTime>=devTime){
				try{
					log.info("query data prevTime{}"+prevTime);
					dealData(prevTime,prevTime+devTime);
				}catch(Exception e){
					log.error("dela history data fail",e);
				}
			}
			log.info("query hostry data over!");
			initFlag = true;
		}
		
	}
	

}
