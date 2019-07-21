package com.young.giv.httpClient;

import com.young.giv.query.Query;
import com.young.giv.utils.GivProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Tornado Young
 */
public class HttpDataFilter {
    private static Log log = LogFactory.getLog(HttpDataFilter.class);
    private static String charset="";
    private PoolingHttpClientConnectionManager connManager;
    static{
        charset = GivProperties.getCharset();
    }
    public HttpDataFilter(){
        this.connManager = HttpRequest.getInstance().getHttpPoolManager();
    }

    public HttpDataFilter(PoolingHttpClientConnectionManager connManager) {
        this.connManager=connManager;
    }

    /**
     * basic method to request content
     * @param url
     * @param map
     * @return
     */
    public ByteBuffer doRequestBasic(String url, Map<String, String> map){
        if(log.isDebugEnabled()){
            log.debug("requst http url{}"+url);
        }
        long startTime = System.currentTimeMillis();
        byte imgdata[] = null;
        ByteBuffer byteBuffer = null;
        InputStream in = null;
        ByteArrayOutputStream bytestream = null;
        try{
//			RequestConfig config = RequestConfig.custom()
//	                .setConnectTimeout(200*1000)
//	                .setSocketTimeout(200*1000)
//	                .setConnectionRequestTimeout(200*1000)
//	                .build();
//
//	        HttpClientBuilder httpClientBuilder = HttpClients.custom()
//	                .setConnectionManager(connManager)
//	                .setDefaultRequestConfig(config);
//	        HttpClient client=httpClientBuilder.build();
            HttpClient client= HttpClientBuilder.create().setConnectionManager(connManager).build();
            HttpPost hp = new HttpPost(url);
            HttpResponse response =null;

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            if (map != null) {
                Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
                }
                if(list.size() > 0){
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                    hp.setEntity(entity);
                }
            }
            response=client.execute(hp);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    in = resEntity.getContent();
                }
            }
            if(log.isDebugEnabled()){
                if(in != null){
                    log.debug("retunr result respose{}"+in.available());
                }else{
                    log.debug("retunr result respose data{}"+null);
                }

            }
            bytestream = new ByteArrayOutputStream();
            int ch;
            if (in != null) {
                while ((ch = in.read()) != -1){
                    bytestream.write(ch);
                }
            }
            imgdata = bytestream.toByteArray();
            if(log.isTraceEnabled()){
                log.trace("return result Binary data{},"+imgdata);
            }
            byteBuffer = ByteBuffer.wrap(imgdata);
            in.close();
        }catch(HttpHostConnectException hoste){
            Query.clearSessionKey();
            log.warn("http request fail!and clear session",hoste);
        }catch(Throwable e){
            log.error("http request fail!",e);
        }finally{
            if(bytestream!=null){
                try {
                    bytestream.close();
                } catch (IOException e) {
                    log.error("close byte stream fail!",e);
                }
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("close input stream fail!",e);
                }
            }
        }

        log.info("request Spend time:"+(System.currentTimeMillis()-startTime)/1000+"s");

        if(log.isDebugEnabled()){
            log.debug("reutrn result buffer size:"+byteBuffer.limit());
        }

        return byteBuffer;
    }
}
