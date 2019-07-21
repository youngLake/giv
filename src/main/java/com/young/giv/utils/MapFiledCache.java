package com.young.giv.utils;

import java.util.HashMap;
import java.util.Map;


public class MapFiledCache {
	
	private static Map<String,String> cache = new HashMap<String,String>();
	
	private static MapFiledCache mapFiledCache = new MapFiledCache();
	
	private static Map<String,Double> cacheCapital = new HashMap<String,Double>();
	
	private static Map<String,Long> cacheTrading = new HashMap<String,Long>();
	
	private static Map<String,Long> cacheTradingSucess = new HashMap<String,Long>();
	
	static{
		cache.put("xxx", "xxx");
	}
	
	private MapFiledCache(){}
	
	public static MapFiledCache getInstance(){
		return mapFiledCache;
	}
	
	public String getAppValue(String key){
		return cache.get(key);
	}
	
	
	public double getCacheCapital(String code){
		double val = cacheCapital.get(code);
		return val;
	}
	
	public long getCacheTrading(String code){
		long val = cacheTrading.get(code);
		return val;
	}
	
	public long getCacheTradingSucess(String code){
		long val = cacheTradingSucess.get(code);
		return val;
	}
	
	
	public synchronized void addCacheCapital(String code,double val){
		if(cacheCapital.containsKey(code)){
			double t = cacheCapital.get(code);
			cacheCapital.put(code, t+val);
		}else{
			cacheCapital.put(code, val);
		}
	}
	
	public synchronized void addCacheTrading(String code,long val){
		if(cacheTrading.containsKey(code)){
			long t = cacheTrading.get(code);
			cacheTrading.put(code, t+val);
		}else{
			cacheTrading.put(code, val);
		}
	}
	
	public synchronized void addCacheTradingSucess(String code,long val){		
		if(cacheTradingSucess.containsKey(code)){
			long t = cacheTradingSucess.get(code);
			cacheTradingSucess.put(code, t+val);
		}else{
			cacheTradingSucess.put(code, val);
		}
	}
	
	public void clearCacheCapital(){
		cacheCapital.clear();
	}
	
	public void clearCacheTrading(){
		cacheTrading.clear();
	}
	
	public void clearCacheTradingSucess(){
		cacheTradingSucess.clear();
	}

}
