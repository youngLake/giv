package com.young.giv.output;

import com.young.giv.utils.GivProperties;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;

import javax.jms.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageSender {
	private static Log log = LogFactory.getLog(MessageSender.class);
	
	private static String MQURL = "";
	private static String MQUSER = "";
	private static String MQPASSWORD = "";
	
	private Connection connection;
	private Session session;
	private MessageProducer appProducer = null;
	private MessageProducer addProducer = null;
	
	static{
		MQURL = GivProperties.getMqUrl();
		MQUSER = GivProperties.getMqUser();
		MQPASSWORD = GivProperties.getMqPassword();
	}
	
	private static BlockingQueue<String> appQueue = new LinkedBlockingQueue<String>();
	private static BlockingQueue<String> addQueue = new LinkedBlockingQueue<String>();
	
	 private static ExecutorService executor;
	
	private static MessageSender messageSender = new MessageSender();
	
	private MessageSender(){
	}
	
	public static MessageSender getInstance(){
		return messageSender;
	}
	
	public void init(){
		try{
			connectMq();
			executor = Executors.newFixedThreadPool(2);
			executor.submit(new Sender(session,appProducer,appQueue));
			executor.submit(new Sender(session,addProducer,addQueue));
		}catch(Exception e){
			log.error("start send message fail",e);
		}
	}
	
	public void putAppKpiQueue(String msg){
		try {
			appQueue.put(msg);
		} catch (InterruptedException e) {
			log.error("appQueue put data fail,msg{}"+msg,e);
		}
	}
	
	
	public void putAppKpiQueue(JSONArray arr){
		try {
			if(arr!=null && arr.length()>0){
			  appQueue.put(arr.toString());
			}
		} catch (InterruptedException e) {
			log.error("appQueue put data fail,msg{}"+arr.toString(),e);
		}
	}
	
    public void putAddrKpiQueue(String msg){
    	try {
    		addQueue.put(msg);
		} catch (InterruptedException e) {
			log.error("appQueue put data fail,msg{}"+msg,e);
		}
	}
    
    public void putAddrKpiQueue(JSONArray arr){
    	try {
    		if(arr!=null && arr.length()>0){
    			addQueue.put(arr.toString());
  			}
    		
		} catch (InterruptedException e) {
			log.error("appQueue put data fail,msg{}"+arr.toString(),e);
		}
	}
	
	public void restConnect(){
		if(session == null){
			connectMq();
		}
		if(connection == null){
			connectMq();
		}
	}
	
	public void connectMq(){
		try {  
			// Create a ConnectionFactory  
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					MQUSER,
					MQPASSWORD,
	                MQURL);;  
			// Create a Connection  
			connection = connectionFactory.createConnection();

			connection.start();
			// Create a Session  
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);  
			// Create the destination (Topic or Queue)  
			Destination appDest= session.createQueue("appKpiQueue");  
			Destination addDest = session.createQueue("addrKpiQueue");  
			
			// Create a MessageProducer from the Session to the Topic or Queue  
			appProducer = session.createProducer(appDest);  
			appProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); 
			
			addProducer = session.createProducer(addDest);  
			addProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);  
		}catch(Exception e){
			log.error("connect mq fail",e);
		}
	}
	
	public void close(){
		try{
			if(appProducer !=null){
				appProducer.close();
			}
			if(addProducer !=null){
				addProducer.close();
			}
			
			if(session!=null){
				session.commit();
				session.close();
			}
			
			if(connection !=null){
				connection.close();
			}
			
		}catch(Exception e){
			log.error("colse MQ connect fail!",e);
		}
	}


  public class Sender implements Runnable{
	private Session session;
	private MessageProducer producer;
	private BlockingQueue<String> queue;
	
	public Sender(Session session,MessageProducer producer,BlockingQueue<String> queue){
		this.session = session;
		this.producer = producer;
		this.queue = queue;
	}

	public void run() {
		 while (true) {
			 String msg = null;
	            try {
	            	msg = queue.take();
	                if (msg != null && !msg.equals("")) {
	                	if(log.isTraceEnabled()){
	                		log.trace("send data:"+msg);
	                	}
	                	 TextMessage message = session
	     	                    .createTextMessage(msg);
	                	 producer.send(message);
	                	 session.commit(); 
	                }
	            } catch(NullPointerException nonePoint){
	            	log.error("send fail ,rest connect mq",nonePoint);
	            	restConnect();
	            }catch(JMSException jms){
	            	log.error("send fail ,rest connect mq",jms);
	            	connectMq();
	            }catch (Exception e) {
	                log.error("send fail,msg{}"+msg, e);
	            }
	        }

	}
	  
  }

}
