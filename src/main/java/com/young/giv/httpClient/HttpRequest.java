package com.young.giv.httpClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.*;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * https ssl
 * @author Tornado Young
 */
public class HttpRequest {
    private static Log log = LogFactory.getLog(HttpRequest.class);

    private static HttpRequest httpRequest = new HttpRequest();
    /**
     * http-client pool
     */
    private PoolingHttpClientConnectionManager poolHttpClient;

    private HttpRequest(){
        try{
            log.info("set http PoolingHttpClientConnectionManager ");
            //init
            poolHttpClient = getPoolingConnectionManager();
        }catch(Exception e){
            log.error("init security fail!",e);
        }
    }

    /**
     * singleton mode - hungry
     * @return
     */
    public static HttpRequest getInstance(){
        return httpRequest;
    }

    /**
     * returning the instance of http-pool-manager
     * @return
     */
    public PoolingHttpClientConnectionManager getHttpPoolManager(){
        return poolHttpClient;
    }

    /**
     * getPoolingConnectionManager , setting SSL certificate for http-connection
     * @return PoolingHttpClientConnectionManager
     * @throws Exception
     */
    private PoolingHttpClientConnectionManager getPoolingConnectionManager(){
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("https", plainSF);
        try {
            //get the specified SSLContext -- TLSv1.2
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            X509TrustManager trustManager = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sc.init(null, new TrustManager[] { trustManager }, null);
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sc,
                    new String[]{"TLSv1","TLSv1.1","TLSv1.2"},
                    new String[]{"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA"},new HostnameVerifier(){
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }});
            registryBuilder.register("https", sslSF);
        } catch (Exception e) {
            log.error("https set fail!,please check this,",e);
            throw new RuntimeException(e);
        }

        PoolingHttpClientConnectionManager  poolManage = new PoolingHttpClientConnectionManager(registryBuilder.build());
        poolManage.setMaxTotal(50);
        return poolManage;
    }


    /**
     * close the client-pool
     */
    public void close(){
        if(poolHttpClient!=null){
            poolHttpClient.close();
            poolHttpClient.shutdown();
        }
    }


    /**
     * adding the third-part provider for assuring requesting https correctly
     */
    private void setSecurity(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

}
