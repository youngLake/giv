package com.young.giv.query;

import com.young.giv.httpClient.HttpDataFilter;
import com.young.giv.output.MessageSender;
import com.young.giv.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Map;


public abstract class Query{
	private static Log log = LogFactory.getLog(Query.class);
	
	private static String locks = "1";
	
	private static String lockMember = "2";
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected static String sessionKey = "";
	
	protected static String[] keys=new String[]{"xxxx"};
	
	//עⲻҪı䵱ǰֵfinal
	private static String loginUrl;
	
	private final static String logoutUrl;
	
	private final static String queryUrl;
	
	protected final static String charset;
	
	protected final static int netlinkid;
	
	protected static String[] nums=new String[]{"","","","","","","","",""};

	static{
		charset = GivProperties.getCharset();
		try {
			loginUrl = GivProperties.getHttpUrl()+"/"+ GivProperties.getLoginIn()+"?param="+ URLEncoder.encode("{\"username\":\"" + GivProperties.getHttpUser() + "\",\"password\":\"" + GivProperties.getHttpPassword()+"\"}",charset);
		} catch (UnsupportedEncodingException e) {
			log.error("loginUrl fail,logUrl{}",e);
		}
		logoutUrl = GivProperties.getHttpUrl()+"/"+ GivProperties.getLogOut()+"?session=";
		queryUrl = GivProperties.getHttpUrl()+"/"+ GivProperties.getQuery()+"?session=";
		netlinkid = GivProperties.getNetlinkid();
	}
	
	/**
	 * ׼ ڳʼ̬ʵʹ
	 */
	public abstract void prepare();

	/**
	 * ִ ̲ѯ
	 */
    public abstract void emit();
    
    /**
     * ͷԴ
     */
    public abstract void release();
    
    /**
     * ȡ
     * @param url
     * @param map
     * @return
     */
    private ByteBuffer getData(String url,Map<String, String> map){
    	
    	HttpDataFilter dataFilter = new HttpDataFilter();
    	
    	return dataFilter.doRequestBasic(url, map);
    }
    
    /**
     *  ȡ
     * @param url
     * @return
     */
    private ByteBuffer getData(String url){
    	return getData(url,null);
    }
    
    /**
     * ˳
     */
    public void toLogOut(){
    	String requestUrl = "";
    	try{
	    	if (sessionKey != null) {
	    		requestUrl = logoutUrl+sessionKey;
	    		getData(requestUrl);
			}
	    	log.info("system user logOut sucess!");
    	}catch(Exception e){
    		log.error("logOut fail,logout url{}"+requestUrl,e);
    	}
    }
    
    /**
     * ȡsessionKey
     */
    public synchronized void toLogIn(){
    	try{
    		ByteBuffer data = getData(loginUrl, null);
			String returnJson = new String(data.array(), charset);
			if (returnJson != null && returnJson.startsWith("{")) {
				JSONObject obj = new JSONObject(returnJson);
				if (obj.has("login_errcode") && obj.getInt("login_errcode") ==0 && obj.has("session")) {
					sessionKey = obj.getString("session");
					log.info("login sucess!session = "+sessionKey);
				} else {
					log.warn("login fail,loginUrl{}"+loginUrl+",return result{}"+returnJson);
				}
			} else {
				log.warn("login fail,loginUrl{}"+loginUrl+",return result{}"+returnJson);
			}
		}catch(Exception e){
			log.error("login fail,logUrl{}"+loginUrl,e);
		}
    }
    
    protected synchronized String getSessionKey(){
    	if(sessionKey == ""){
    		toLogIn();
    	}
    	if(log.isDebugEnabled()){
    		log.debug("get sessionKey{}"+sessionKey);
    	}
    	return sessionKey;
    }
    
    public static synchronized void clearSessionKey(){
    	synchronized (locks) {
    		sessionKey = "";
		} 
    }
    
    /**
     * ȡѯurl
     * @param param
     * @return
     */
    protected synchronized String getQueryUrl(String param){
    	//session Ϊʱ
    	synchronized (locks) {
    		if(sessionKey == ""){
        		toLogIn();
        	}
        	return queryUrl+sessionKey+"&param="+param;
		}    	
    }
    
    /**
     * ȡгԱУȡΪգ5
     * @return
     */
    protected synchronized Map<Long,String> getMembers(){
    	Map<Long,String> members = NcisrMemberCache.getInstance().getAllMember();
    	synchronized (lockMember) {
	    	if(members.size()<1){
//	    		int i =0;
//	    		while(i<5){ //5ӣȡΪֹ
//	    			QueryNcisrMember queryMember =new QueryNcisrMember();
//	    			queryMember.emit();
//	    			i++; 
//	    			members = NcisrMemberCache.getInstance().getAllMember();
//	    			if(members.size()>1){
//	    				return members;
//	    			}
//	    		}
	    	}
    	}
    	return members;
    }
    
    
   /**
    * װͼĽݼ
    * @param startCode
    * @param endCode
    * @param total_count
    * @param success_count
    */
    protected void assmebVolumeData(String startCode,String endCode,BigInteger total_count,long success_count){
    	if(startCode == null || startCode.equals("")||endCode == null || endCode.equals("")){
    		log.warn("assmebData fail,startCode or endCode is null");
    		return ;
    	}
    	
    	if(log.isTraceEnabled()){
    		log.trace("assmebVolumeData data{},startCode{}"+startCode+",endCode{}"+endCode+",total_count{},"+total_count+",success_count{}"+success_count);
    	}
    	try{
	    	if(startCode == endCode){
	    	  MapFiledCache.getInstance().addCacheTrading(startCode, total_count.longValue());
	      	  MapFiledCache.getInstance().addCacheTradingSucess(startCode, success_count);
	    	}else{
	    		 MapFiledCache.getInstance().addCacheTrading(startCode, total_count.longValue());
	         	 MapFiledCache.getInstance().addCacheTradingSucess(startCode, success_count);
	         	 
	         	 MapFiledCache.getInstance().addCacheTrading(endCode, total_count.longValue());
	        	 MapFiledCache.getInstance().addCacheTradingSucess(endCode, success_count);
	    	}
    	}catch(Exception e){
    		log.error("assmebVolumeData data fail,startCode{}"+startCode+",endCode{}"+endCode,e);
    	}
    }
    

    protected void assmebCapitalData(String startCode,String endCode,double atm){
    	if(startCode == null || startCode.equals("")||endCode == null || endCode.equals("")){
    		log.warn("assmebData fail,startCode or endCode is null");
    		return ;
    	}
    	if(log.isTraceEnabled()){
    		log.trace("assmebVolumeData data{},startCode{}"+startCode+",endCode{}"+endCode+",atm{},"+atm);
    	}
    	try{
	    	if(startCode == endCode){
	    	  MapFiledCache.getInstance().addCacheCapital(startCode, atm);
	    	}else{
	    		 MapFiledCache.getInstance().addCacheCapital(startCode, atm);
	         	 
	         	 MapFiledCache.getInstance().addCacheCapital(endCode, atm);
	    	}
    	}catch(Exception e){
    		log.error("assmebVolumeData data fail,startCode{}"+startCode+",endCode{}"+endCode,e);
    	}
    }
    

    protected void sendTradingVolume(String startCode,String endCode,String key,long date){
    	if(startCode == null || startCode.equals("")||endCode == null || endCode.equals("")){
    		log.warn("assmebData fail,startCode or endCode is null");
    		return ;
    	}
    	try{
	    	JSONArray arrJson=new JSONArray();
	    	if(startCode == endCode){
	    		  JSONObject tps = new JSONObject();
	    	  	  tps.put("address", startCode);
	    	  	  tps.put("app", MapFiledCache.getInstance().getAppValue(key));
	    	  	  tps.put("unit", "[,,]");
	    	  	  tps.put("date", DatetimeUtil.formatDate(date));
	    	  	  tps.put("formatdate", formatDate(date));
	    	  	  tps.put("value", "["+MapFiledCache.getInstance().getCacheTrading(startCode)+","+MapFiledCache.getInstance().getCacheTradingSucess(startCode)+","+formatDouble(MapFiledCache.getInstance().getCacheCapital(endCode))+"]");
	    	  	  tps.put("kpi", "");
	    	  	  arrJson.put(tps);
	    	}else{
	    		 JSONObject volume = new JSONObject();
	    		 volume.put("address", startCode);
	    		 volume.put("app", MapFiledCache.getInstance().getAppValue(key));
	    		 volume.put("unit", "[,,]");
	    		 volume.put("date", DatetimeUtil.formatDate(date));
	    		 volume.put("formatdate", formatDate(date));
	    		 volume.put("value", "["+MapFiledCache.getInstance().getCacheTrading(startCode)+","+MapFiledCache.getInstance().getCacheTradingSucess(startCode)+","+formatDouble(MapFiledCache.getInstance().getCacheCapital(endCode))+"]");
	    		 volume.put("kpi", "");
	   	  	     arrJson.put(volume);
	   	  	  
	   	  	  
		   	      JSONObject tps = new JSONObject();
			  	  tps.put("address", endCode);
			  	  tps.put("app", MapFiledCache.getInstance().getAppValue(key));
			  	  tps.put("unit", "[,,]");
			  	  tps.put("date", DatetimeUtil.formatDate(date));
			  	  tps.put("formatdate", formatDate(date));
			  	  tps.put("value", "["+MapFiledCache.getInstance().getCacheTrading(endCode)+","+MapFiledCache.getInstance().getCacheTradingSucess(endCode)+","+formatDouble(MapFiledCache.getInstance().getCacheCapital(endCode))+"]");
			  	  tps.put("kpi", "");
			  	  arrJson.put(tps);
	    	}
	    	 MessageSender.getInstance().putAddrKpiQueue(arrJson.toString());
    	}catch(Exception e){
    		log.error("assmebVolumeData data fail,startCode{}"+startCode+",endCode{}"+endCode,e);
    	}
    }
    
    protected String formatDate(long time){
    	return dateFormat.format(time);
    }
    
    protected String formatDouble(double d){
    	try{
    		 String s = Double.toString(d);
    		 return s.substring(0,s.indexOf("."));
    	}catch(Exception e){
    		log.error("format double error!",e);
    	}
    	return "0";
    }
    

}
