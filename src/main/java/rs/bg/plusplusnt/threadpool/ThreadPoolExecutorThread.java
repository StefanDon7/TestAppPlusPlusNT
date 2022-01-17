/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.threadpool;

import java.util.concurrent.TimeUnit;
import rs.bg.plusplusnt.domen.runnable.PacketRunnable;

/**
 *
 * @author Stefan
 */
public class ThreadPoolExecutorThread {

    private ThreadPoolExecutor threadPoolExecutor;
    private final static ThreadPoolExecutorThread instance = new ThreadPoolExecutorThread();

    private ThreadPoolExecutorThread() {
        threadPoolExecutor = new ThreadPoolExecutor();
    }

    public static ThreadPoolExecutorThread getInstance() {
        return instance;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void getFromQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final PacketRunnable packetRunnable = threadPoolExecutor.getQueue().poll();
                    if (packetRunnable != null) {
                        threadPoolExecutor.getScheduledExecutorService().schedule(packetRunnable, packetRunnable.getPacket().getDelay(), TimeUnit.SECONDS);
                    }
                }
            }
        }).start();
    }
    
    public void start(){
        getFromQueue();
    }

}
