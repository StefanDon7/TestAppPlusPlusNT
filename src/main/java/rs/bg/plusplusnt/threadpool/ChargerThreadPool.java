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
public class ChargerThreadPool {

    private ThreadPool threadPool;
    private final static ChargerThreadPool instance = new ChargerThreadPool();

    private ChargerThreadPool() {
        threadPool = new ThreadPool();
    }

    public static ChargerThreadPool getInstance() {
        return instance;
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public void getFromQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final PacketRunnable packetRunnable = threadPool.getQueue().poll();
                    if (packetRunnable != null) {
                        threadPool.getScheduledExecutorService().schedule(packetRunnable, packetRunnable.getPacket().getTimeToExecute(), TimeUnit.MILLISECONDS);
                    }
                }
            }
        }).start();
    }

    public void start() {
        getFromQueue();
    }

}
