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
import rs.bg.plusplusnt.domen.IPacket;
import rs.bg.plusplusnt.domen.runnable.PacketRunnable;

/**
 *
 * @author Stefan
 */
public class ThreadPoolExecutor {

    private final BlockingQueue<PacketRunnable> queue = new ArrayBlockingQueue<>(50);
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(15);

    public ThreadPoolExecutor() {
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public BlockingQueue<PacketRunnable> getQueue() {
        return queue;
    }
   public void addToQueue(IPacket iPacket) {
        ThreadPoolExecutorThread.getInstance().getThreadPoolExecutor().getQueue().add(new PacketRunnable(iPacket));
    }

}
