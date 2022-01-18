/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.communication.thread;

import rs.bg.plusplusnt.communication.CommunicationService;
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
    private CommunicationService communicationService;

    private CommunicationWithServerThread() {
        communicationService = new CommunicationWithServer();
    }

    public CommunicationService getCommunicationService() {
        return communicationService;
    }

    public static CommunicationWithServerThread getInstance() {
        return instance;
    }

    public void receivePackages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Packet packet = communicationService.getPacketFromServer();
                    if (packet != null) {
                        switch (packet.getType()) {
                            case Dummy:
                                ControllerDB.getInstance().savePacket(packet);
                                ChargerThreadPool.getInstance().getPacketQueue().addToQueue(packet);
                                break;
                            case Cancel:
                                communicationService.bringPacketBackToServer(packet);
                                break;
                            default:
                                break;
                        }
                        System.out.println(packet);
                    }
                }
            }
        }
        ).start();
    }

    public void startThread() {
        CommunicationWithServerThread.getInstance().receivePackages();
    }
}
