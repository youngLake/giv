package com.young.giv.query;

import com.young.giv.httpClient.HttpDataFilter;
import com.young.giv.output.MessageSender;
import com.young.giv.utils.DataParseUtil;
import com.young.giv.utils.DatetimeUtil;
import com.young.giv.utils.MapFiledCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class QueryTimeout extends Query {
	private static Log log = LogFactory.getLog(QueryTimeout.class);

	@Override
	public void prepare() {

	}

	@Override
	public void emit() {

	}
	
	
	private void assmebTimeOutData(String startCode,String endCode,String key,long date,long value,JSONArray arrJson){}

	@Override
	public void release() {

	}

}
 