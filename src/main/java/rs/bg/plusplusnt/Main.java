/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt;

import java.io.IOException;
import java.util.List;
import rs.bg.plusplusnt.communication.thread.CommunicationWithServerThread;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.IPacket;
import rs.bg.plusplusnt.threadpool.ThreadPoolExecutorThread;

/**
 *
 * @author Stefan
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<IPacket> lista = ControllerDB.getInstance().getAll();
        for (IPacket packet : lista) {
            if (packet.hasExpired()) {
                CommunicationWithServerThread.getInstance().getCommunicationWithServer().sendMessageToServer(packet);
                ControllerDB.getInstance().deletePacket(packet);
            } else {
                ThreadPoolExecutorThread.getInstance().getThreadPoolExecutor().addToQueue(packet);
                packet.setNewDelay();
                System.out.println("Packet with id:" + packet.getID() + " add to queue.");
            }
        }
        CommunicationWithServerThread.getInstance().start();
        ThreadPoolExecutorThread.getInstance().start();
    }
}
