package com.young.giv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkThreadPool {
	private Log log = LogFactory.getLog(getClass());

    private static final int DEFALUT_THREAD_POOL_SIZE = 10;

    private static final int DEFALUT_SCHDULE_SIZE= 5;

    private ExecutorService exec;
    private ScheduledExecutorService sexec;

    private static final WorkThreadPool workThreadPool =new WorkThreadPool();

    private WorkThreadPool(){
    	exec = Executors.newFixedThreadPool(DEFALUT_THREAD_POOL_SIZE);
        sexec = Executors.newScheduledThreadPool(DEFALUT_SCHDULE_SIZE);
    }

    public void execute(Runnable run){
        exec.execute(run);
    }

    public static WorkThreadPool getInstance(){
        return workThreadPool;
    }

    public ScheduledExecutorService getSchedulePool(){
        return sexec;
    }

    public void shutdownNow() {
        exec.shutdown();
        try {
            if(!exec.awaitTermination(10, TimeUnit.SECONDS)){
                exec.shutdownNow();
            }
        } catch (Exception e) {
            log.error(e);
            exec.shutdownNow();
        }

    }

}
