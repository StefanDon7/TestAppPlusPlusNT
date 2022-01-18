/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.communication.thread;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.bg.plusplusnt.communication.CommunicationWithServer;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.threadpool.ChargerThreadPool;

/**
 *
 * @author Stefan
 */
public class CommunicationWithServerThread {

    public static final CommunicationWithServerThread instance = new CommunicationWithServerThread();
    private CommunicationWithServer communicationWithServer;

    private CommunicationWithServerThread() {
        communicationWithServer = new CommunicationWithServer();
    }

    public static CommunicationWithServerThread getInstance() {
        return instance;
    }

    public CommunicationWithServer getCommunicationWithServer() {
        return communicationWithServer;
    }

    public void receivePackages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Packet packet = communicationWithServer.getPacketFromServer();
                        switch (packet.getType()) {
                            case Dummy:
                                ControllerDB.getInstance().savePacket(packet);
                                ChargerThreadPool.getInstance().getPacketQueue().addToQueue(packet);
                                break;
                            case Cancel:
                                communicationWithServer.bringPacketBackToServer(packet);
                                break;
                            default:
                                break;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(getCommunicationWithServer().getPacketMaker().getPacket());
                }
            }
        }).start();
    }

    public void start() {
        CommunicationWithServerThread.getInstance().receivePackages();
    }
}
