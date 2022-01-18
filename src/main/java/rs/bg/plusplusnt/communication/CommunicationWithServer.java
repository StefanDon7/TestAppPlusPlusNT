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
import rs.bg.plusplusnt.makers.ByteHandler;
import rs.bg.plusplusnt.makers.PacketMaker;
import rs.bg.plusplusnt.convertor.Convertor;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.filereader.SettingsLoader;
import rs.bg.plusplusnt.threadpool.ChargerThreadPool;

/**
 *
 * @author Stefan
 */
public class CommunicationWithServer implements CommunicationService {

    private PacketMaker packetMaker;
    private ByteHandler byteHandler;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socketForCommunitation;

    public CommunicationWithServer() {
        try {
            createConnection();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSocketForCommunitation(Socket socketForCommunitation) {
        this.socketForCommunitation = socketForCommunitation;
    }

    public void createConnection() throws IOException {
        String serverURL = SettingsLoader.getInstance().getDatabaseProperty("serverURL");
        String serverPortString = SettingsLoader.getInstance().getDatabaseProperty("serverPort");
        int serverPort = Integer.parseInt(serverPortString);
        socketForCommunitation = new Socket(serverURL, serverPort);
    }

    public byte getByteFromServer() throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        return in.readByte();
    }

    public void getByteFromStream(byte[] bytes, int start) throws IOException {
        for (int i = start; i < bytes.length; i++) {
            bytes[i] = getByteFromServer();
        }
    }

    private void createByteHandler() throws IOException {
        byteHandler = new ByteHandler();
        getByteFromStream(byteHandler.getHeader(), 0);
        switch (Convertor.byteArraytoIntLE(byteHandler.getHeader())) {
            case 1:
                byteHandler.setFull(16);
                break;
            case 2:
                byteHandler.setFull(12);
                break;
            default:
                byteHandler.setFull(256);
                break;
        }
        getByteFromStream(byteHandler.getFull(), byteHandler.getHeader().length);
    }

    private void createPacketMaker() {
        packetMaker = new PacketMaker(byteHandler);
        packetMaker.createPacket();
    }

    @Override
    public Packet getPacketFromServer() {
        try {
            createByteHandler();
            createPacketMaker();
            return packetMaker.getPacket();
        } catch (IOException ex) {
            recoverConnection();
            checkUnsendPacketFromDatabase();
        }
        return null;
    }

    private void recoverConnection() {
        try {
            socketForCommunitation.close();
            while (socketForCommunitation.isClosed()) {
                try {
                    createConnection();
                    Thread.sleep(1000);
                } catch (IOException ex1) {
                    System.out.println("Trying to get connection...");
                } catch (InterruptedException ex1) {
                    Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (IOException ex1) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    @Override
    public void bringPacketBackToServer(Packet packet) {
        try {
            out = new DataOutputStream(socketForCommunitation.getOutputStream());
            out.write(packet.getPacketArray());
            System.out.println("Packet with id:" + packet.getID() + " bring back to server.");
        } catch (IOException ex) {
            Logger logger = Logger.getLogger(CommunicationWithServer.class.getName());
            logger.log(Level.INFO, "Packet with id:" + packet.getPacketID() + " unsuccessful sent to server!");
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void sendMessageToServer(Packet packet) {
        try {
            out = new DataOutputStream(socketForCommunitation.getOutputStream());
            out.writeBytes("Packet with id:" + packet.getID() + " has expired.");
            System.out.println("Packet with id:" + packet.getID() + " has expired.");
        } catch (IOException ex) {
            Logger logger = Logger.getLogger(CommunicationWithServer.class.getName());
            logger.log(Level.INFO, "Unsuccessful send notification to server.");
        }
    }

    @Override
    public void checkUnsendPacketFromDatabase() {
        List<Packet> lista = ControllerDB.getInstance().getAll();
        for (Packet packet : lista) {
            if (packet.hasExpired()) {
                sendMessageToServer(packet);
                ControllerDB.getInstance().deletePacket(packet);
            } else {
                ChargerThreadPool.getInstance().getPacketQueue().addToQueue(packet);
                System.out.println("Packet with id:" + packet.getID() + " add to queue.");
            }
        }
    }
}
