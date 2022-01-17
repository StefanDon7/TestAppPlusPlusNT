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
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.bg.plusplusnt.convertor.ByteHandler;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.IPacket;
import rs.bg.plusplusnt.domen.runnable.PacketRunnable;
import rs.bg.plusplusnt.threadpool.ThreadPoolExecutor;

/**
 *
 * @author Stefan
 */
public class CommunitationWithServer {

    private static final CommunitationWithServer instance = new CommunitationWithServer();

    private ByteHandler byteHandler = new ByteHandler();
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socketForCommunitation;

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
                    System.out.println(byteHandler.getPacket());
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

    public void getBytesArrayFromServer(byte[] bytes) throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        in.read(bytes);
    }

    public byte getByteFromServer() throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        return in.readByte();
    }

    public void getPacket() throws IOException {
        byteHandler.allocationBuffer();
        byteHandler.setAllAtributes();
        ControllerDB.getInstance().savePacket(byteHandler.getPacket());
        addToQueue(byteHandler.getPacket());
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

    public void addToQueue(IPacket iPacket) {
        ThreadPoolExecutor.getInstance().getQueue().add(new PacketRunnable(iPacket));
    }
}
