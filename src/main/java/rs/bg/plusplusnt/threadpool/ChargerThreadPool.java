/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import rs.bg.plusplusnt.domen.runnable.PacketRunnable;

/**
 *
 * @author Stefan
 */
public class ChargerThreadPool {

    private final PacketQueue packetQueue;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(15);
    private final static ChargerThreadPool instance = new ChargerThreadPool();

    private ChargerThreadPool() {
        packetQueue = new PacketQueue();
    }

    public static ChargerThreadPool getInstance() {
        return instance;
    }

    public PacketQueue getPacketQueue() {
        return packetQueue;
    }

    public void addToSchedule() {
        new Thread(() -> {
            while (true) {
                final PacketRunnable packetRunnable = getPacketQueue().getQueue().poll();
                if (packetRunnable != null) {
                    scheduledExecutorService.schedule(packetRunnable, packetRunnable.getPacket().getTimeToExecute(), TimeUnit.MILLISECONDS);
                }
            }
        }).start();
    }

    public void start() {
        addToSchedule();
    }

}
