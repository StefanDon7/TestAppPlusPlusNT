/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.domen.runnable.PacketRunnable;

/**
 *
 * @author Stefan
 */
public class PacketQueue {

   private final BlockingQueue<PacketRunnable> queue = new LinkedBlockingQueue<>();
 
    public BlockingQueue<PacketRunnable> getQueue() {
        return queue;
    }

    public void addToQueue(Packet packet) {
       queue.add(new PacketRunnable(packet));
    }

}
