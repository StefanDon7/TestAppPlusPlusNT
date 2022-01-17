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
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.threadpool.ChargerThreadPool;

/**
 *
 * @author Stefan
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<Packet> lista = ControllerDB.getInstance().getAll();
        for (Packet packet : lista) {
            if (packet.hasExpired()) {
                CommunicationWithServerThread.getInstance().getCommunicationWithServer().sendMessageToServer(packet);
                ControllerDB.getInstance().deletePacket(packet);
            } else {
                ChargerThreadPool.getInstance().getThreadPool().addToQueue(packet);
                System.out.println("Packet with id:" + packet.getID() + " add to queue.");
            }
        }
        CommunicationWithServerThread.getInstance().start();
        ChargerThreadPool.getInstance().start();
    }
}
