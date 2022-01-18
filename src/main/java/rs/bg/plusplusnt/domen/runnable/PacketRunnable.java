/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.domen.runnable;

import rs.bg.plusplusnt.communication.thread.CommunicationWithServerThread;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.Packet;

/**
 *
 * @author Stefan
 */
public class PacketRunnable implements Runnable {

    private Packet packet;

    public PacketRunnable(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    @Override
    public void run() {
        CommunicationWithServerThread.getInstance().getCommunicationService().bringPacketBackToServer(packet);
        ControllerDB.getInstance().deletePacket(packet);
    }

}
