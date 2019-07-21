package com.young.giv.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class DataParseUtil {
	private static Log log = LogFactory.getLog(DataParseUtil.class);

	public static String getString(ByteBuffer buffer,String charset){
		try {
			int strLength = buffer.getInt();
			if (strLength > 0) {
				byte[] str = new byte[strLength];
				buffer.get(str);
				return new String(str, charset);
			}
		} catch (UnsupportedEncodingException e){
			log.error("buffer change string fail!",e);
		}	
		return "";
	}
	
	public static int getShort(ByteBuffer buffer){
		return buffer.getShort() & 0xFFFF;
	}
	
	public static long getInt(ByteBuffer buffer){
		return buffer.getInt() & 0xffffffff;
	}
	
	public static int getByte(ByteBuffer buffer){
		return buffer.get() & 0xFF;
	}
	
	public static BigInteger getLong(ByteBuffer buffer){
		return BigInteger.valueOf(buffer.getLong() & 0xffffffffffffffffL);
	}
	
	public static double getTransDataValue(ByteBuffer buffer){
		Map<String,String> result = getTransData(buffer);
		String amt = result.get("Amt");
		try{
		   return Double.parseDouble(amt);
		}catch(Exception e){
			log.error("getTransDataValue fail,amt{}"+amt,e);
			return 0L;
		}
	}
	
	public static Map<String,String> getTransData(ByteBuffer buffer){
		Map<String,String> result = new HashMap<String,String>();
		try{
			int strLength=0;
			strLength=(int)(((buffer.get() & 0xff))
					|((buffer.get() & 0xff) << 8)
					|((buffer.get() & 0xff) << 16)
					|((buffer.get() & 0xff) << 24));
			if(strLength>0){
				byte[] bytes=new byte[(int)strLength];
				buffer.get(bytes,0,(int)strLength);
				ByteBuffer tmpBuffer=ByteBuffer.wrap(bytes);
				while(tmpBuffer.hasRemaining()){
					byte b=tmpBuffer.get();
					if(b==0){
						return result;
					}
					byte[] str=new byte[b];
					tmpBuffer.get(str,0,b);
					String name=new String(str,0,b,"UTF-8");
					String value="";
					tmpBuffer.get();
					byte h = tmpBuffer.get();
					byte l = tmpBuffer.get();
					short s=(short)((h & 0xff)|((l & 0xff) << 8));
					
					if(s>0){
						str=new byte[s];
						tmpBuffer.get(str,0,s);
						value=new String(str,0,s,"UTF-8");
					}
					if(name!=null&&name.contains(":")){
						name=name.split(":")[1];
					}
					result.put(name, value);

					if(log.isDebugEnabled()){
						log.debug("return data,"+name+"-"+value);
					}
				}
			}
		}catch(Exception e){
			log.error("parse data fail",e);
		}
		return result;
	}
	

}
