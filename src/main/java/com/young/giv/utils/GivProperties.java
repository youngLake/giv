package com.young.giv.utils;

import org.apache.activemq.ActiveMQConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GivProperties {
	private static Log logger = LogFactory.getLog(GivProperties.class);
    private static Properties conf = new Properties();

    private static final String HOSTNAME = "https://127.0.0.1:8080";
    private static final String MqUrl = "tcp://127.0.0.1:61616";
    private static final String CONFIG_FILE = "config.properties";

    public static String getProperty(String key) {
        return conf.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return conf.getProperty(key, defaultValue);
    }

    public static String getHttpUrl() {
        String confClusterName = conf.getProperty("hostname", HOSTNAME);
        return confClusterName;
    }
    
    public static String getHttpUser(){
    	String userName = conf.getProperty("http_user", "123456");
        return userName;
    }
    
    public static String getHttpPassword(){
    	String password = conf.getProperty("http_password", "123456");
        return password;
    }
    
    public static String getLoginIn(){
    	String login = conf.getProperty("login_suffix", "login.php");
        return login;
    }
    
    public static String getQuery(){
    	String query = conf.getProperty("query_suffix", "statsquery.php");
        return query;
    }
    
    public static String getLogOut(){
    	String logout = conf.getProperty("logout_suffix", "logout.php");
        return logout;
    }
    
    public static String getCharset(){
    	String charset = conf.getProperty("charset", "UTF-8");
        return charset;
    }

    public static Integer getFrequency1Time(){
    	String frequencyTime = conf.getProperty("frequency1Time", "10");
    	int intRT = 60;

        try {
            intRT = Integer.parseInt(frequencyTime);
        } catch (Exception var3) {
            logger.error(var3.getMessage(), var3);
        }

        return intRT;
    }
    
    public static Integer getFrequency2Time(){
    	String frequencyTime = conf.getProperty("frequency2Time", "10");
    	int intRT = 60;

        try {
            intRT = Integer.parseInt(frequencyTime);
        } catch (Exception var3) {
            logger.error(var3.getMessage(), var3);
        }

        return intRT;
    }

    public static Integer getFrequency2TimeTpm(){
    	String frequencyTime = conf.getProperty("frequency2TimeTpm", "60");
    	int intRT = 60;

        try {
            intRT = Integer.parseInt(frequencyTime);
        } catch (Exception var3) {
            logger.error(var3.getMessage(), var3);
        }

        return intRT;
    }
    
    
    public static String getMqUrl(){
    	String mqUrl = conf.getProperty("mq_url", MqUrl);
        return mqUrl;
    }
    
    public static String getMqUser(){
    	String mqUser = null;
    	try {
    		mqUser = conf.getProperty("mq_user", ActiveMQConnection.DEFAULT_USER);
        	if(mqUser.equals("")){
        		return ActiveMQConnection.DEFAULT_USER;
        	}
        } catch (Exception var3) {
        	return ActiveMQConnection.DEFAULT_USER;
        }
    	
        return mqUser;
    }
    
    public static String getMqPassword(){
    	String mqPassword = null;
    	try {
    		mqPassword = conf.getProperty("mq_password", ActiveMQConnection.DEFAULT_PASSWORD);
        	if(mqPassword.equals("")){
        		return ActiveMQConnection.DEFAULT_PASSWORD;
        	}
        } catch (Exception var3) {
        	return ActiveMQConnection.DEFAULT_PASSWORD;
        }
    	
        return mqPassword;
    }
    
    public static int getNetlinkid(){
    	String netlinkid = conf.getProperty("netlinkid", "7");
    	int intRT = 60;

        try {
            intRT = Integer.parseInt(netlinkid);
        } catch (Exception var3) {
            logger.error(var3.getMessage(), var3);
        }

        return intRT;
    }
    
    static {
        Object in = null;
            try {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
            } catch (Exception var16) {
                logger.warn("config file not found in classpath. " + var16);
            }

        if(in != null) {
            try {
                conf.load((InputStream)in);
            } catch (IOException var14) {
                logger.warn("load config failed." + var14);
            } finally {
                try {
                    ((InputStream)in).close();
                } catch (IOException var13) {
                    logger.warn("Close inputstream failed.");
                }

            }
        }

    }
}
