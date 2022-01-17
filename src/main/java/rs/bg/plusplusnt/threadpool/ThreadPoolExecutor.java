/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import rs.bg.plusplusnt.domen.runnable.PacketRunnable;

/**
 *
 * @author Stefan
 */
public class ThreadPoolExecutor {

    private static final ThreadPoolExecutor instance = new ThreadPoolExecutor();

    private final BlockingQueue<PacketRunnable> queue = new ArrayBlockingQueue<>(50);
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(15);

    private ThreadPoolExecutor() {
    }

    public static ThreadPoolExecutor getInstance() {
        return instance;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public BlockingQueue<PacketRunnable> getQueue() {
        return queue;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final PacketRunnable packetRunnable = queue.poll();
                    if (packetRunnable != null) {
                        scheduledExecutorService.schedule(packetRunnable, packetRunnable.getPacket().getDelay(), TimeUnit.SECONDS);
                    }
                }
            }
        }).start();
    }

}
