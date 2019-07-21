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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class QueryStatsSum extends Query {
	private Log log = LogFactory.getLog(QueryStatsSum.class);
	private static long prevTime;

	@Override
	public void prepare() {
		prevTime = DatetimeUtil.getCurrentDayPrecision();
	}

	@Override
	public void emit() {}

	@Override
	public void release() {

	}

}
