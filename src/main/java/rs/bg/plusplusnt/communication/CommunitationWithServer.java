/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.bg.plusplusnt.convertor.ByteHandler;
import rs.bg.plusplusnt.convertor.Convertor;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.Cancel;
import rs.bg.plusplusnt.domen.Dummy;
import rs.bg.plusplusnt.domen.IPacket;
import rs.bg.plusplusnt.domen.Type;
import rs.bg.plusplusnt.domen.runnable.PacketRunnable;
import rs.bg.plusplusnt.threadpool.ThreadPoolExecutor;

/**
 *
 * @author Stefan
 */
public class CommunitationWithServer {

    private static final CommunitationWithServer instance = new CommunitationWithServer();

    IPacket packet;
    ByteHandler byteHandler = new ByteHandler();
    DataInputStream in;
    DataOutputStream out;
    Socket socketForCommunitation;

    private CommunitationWithServer() {
        try {
            createConnection();
        } catch (IOException ex) {
            Logger.getLogger(CommunitationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static CommunitationWithServer getInstance() {
        return instance;
    }

    public void receivePackages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        getPacket();
                    } catch (IOException ex) {
                        Logger.getLogger(CommunitationWithServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(packet);
                }
            }
        }).start();
    }

    public void start() {
        CommunitationWithServer.getInstance().receivePackages();
    }

    public void createConnection() throws IOException {
        socketForCommunitation = new Socket("hermes.plusplus.rs", 4000);
    }

    public void getBytesFromServer(byte[] bytes) throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        in.read(bytes);
    }

    public byte getByteFromServer() throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        return in.readByte();
    }

    public void getPacket() throws IOException {
        allocationBuffer();
        setAllAtributes();
        ControllerDB.getInstance().savePacket(packet);
        addToQueue(packet);
    }

    public void bringPacketBackToServer(IPacket packet) throws IOException {
        out = new DataOutputStream(socketForCommunitation.getOutputStream());
        out.write(packet.getPacketArray());
        System.out.println("Packet with id:" + packet.getID() + " bring back to server.");
    }

    public void sendMessageToServer(IPacket packet) throws IOException {
        out = new DataOutputStream(socketForCommunitation.getOutputStream());
        out.writeBytes("Packet with id:" + packet.getID() + " has expired.");
        System.out.println("Packet with id:" + packet.getID() + " has expired.");
    }

    private void allocationBuffer() throws IOException {
        getBytesFromServer(byteHandler.getHeader());
        int packetID = Convertor.byteArraytoIntLE(byteHandler.getHeader());
        switch (packetID) {
            case 1:
                System.out.println("*****Dummy paket*****");
                byteHandler.setFull(16);
                packet = new Dummy();
                packet.setType(Type.Dummy);
                packet.setPacketID(packetID);
                break;
            case 2:
                System.out.println("*****Cancel paket*****");
                byteHandler.setFull(12);
                packet = new Cancel();
                packet.setType(Type.Cancel);
                packet.setPacketID(packetID);
                break;
            default:
        }
        byteHandler.fillFull();
    }

    private void setAllAtributes() {
        packet.setPacketArray(byteHandler.getFull());
        packet.setLength(Convertor.byteArraytoIntLE(byteHandler.copyByte(byteHandler.getFull(), 4, 8)));
        packet.setID(Convertor.byteArraytoIntLE(byteHandler.copyByte(byteHandler.getFull(), 8, 12)));
        System.out.println(packet.getID());
        switch (packet.getType()) {
            case Dummy:
                packet.setDelay(Convertor.byteArraytoIntLE(byteHandler.copyByte(byteHandler.getFull(), 12, 16)));
                packet.setPacketExpiration(packet.createPacketExpiration(packet.getDelay()));
                break;
            case Cancel:
                packet.setDelay(0);
                packet.setPacketExpiration(packet.createPacketExpiration(packet.getDelay()));
                break;
            default:
        }
    }

    public void addToQueue(IPacket iPacket) {
        ThreadPoolExecutor.getInstance().getQueue().add(new PacketRunnable(iPacket));
    }
}
