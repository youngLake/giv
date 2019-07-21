package com.young.giv.utils;

import java.util.HashMap;
import java.util.Map;

public class NcisrMemberCache {
  private static Map<Long,String> cache = new HashMap<Long,String>();
	
	private static NcisrMemberCache mapMemberCache = new NcisrMemberCache();
	
	private NcisrMemberCache(){}
	
	public static NcisrMemberCache getInstance(){
		return mapMemberCache;
	}
	

	public Map<Long,String> getAllMember(){
		return cache;
	}
	
	public String getAppValue(String key){
		return cache.get(key);
	}
	
	public synchronized void put(long key,String value){
//		if(!cache.containsKey(key)){
//			cache.put(key,value);
//		}
		cache.put(key,value);
	}
}
