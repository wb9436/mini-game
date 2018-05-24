package com.zhiyou.wxgame.server.runnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.zhiyou.wxgame.configuration.ApplicationBeanManager;
import com.zhiyou.wxgame.server.card.Table;
import com.zhiyou.wxgame.server.service.ITableService;

public abstract class AbstractRunnable implements Runnable{

    protected static ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
    
    protected ScheduledFuture<?> timeoutFuture;
    protected Table table;
    
    protected ITableService tableService  = ApplicationBeanManager.getBean("tableService");
    protected final Logger logger = Logger.getLogger(getClass());
    
    /**
     * 关掉线程
     */
    protected void stopTimeoutThread() {
        if (timeoutFuture != null && !timeoutFuture.isCancelled()) {
            timeoutFuture.cancel(true);
        }
    }

    /**
     * 隔段时间继续运行
     * @param timeout
     */
    protected void startTimeoutThread(int timeout) {
        this.timeoutFuture = threadPool.schedule(this, timeout, TimeUnit.SECONDS);
    }
    
    /**
     * 隔段时间继续运行
     * @param timeout
     */
    protected void startTimeoutMilliThread(int timeout) {
        this.timeoutFuture = threadPool.schedule(this, timeout, TimeUnit.MILLISECONDS);
    }
}
