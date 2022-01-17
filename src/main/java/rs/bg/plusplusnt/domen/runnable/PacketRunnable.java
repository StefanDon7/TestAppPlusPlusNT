/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.domen.runnable;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            CommunicationWithServerThread.getInstance().getCommunicationWithServer().bringPacketBackToServer(packet);
            ControllerDB.getInstance().deletePacket(packet);
        } catch (IOException ex) {
            Logger.getLogger(PacketRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
