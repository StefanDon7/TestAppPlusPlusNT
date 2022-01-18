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
import rs.bg.plusplusnt.makers.ByteHandler;
import rs.bg.plusplusnt.makers.PacketMaker;
import rs.bg.plusplusnt.convertor.Convertor;
import rs.bg.plusplusnt.domen.Packet;

/**
 *
 * @author Stefan
 */
public class CommunicationWithServer {

    private PacketMaker packetMaker;
    private ByteHandler byteHandler;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socketForCommunitation;

    public PacketMaker getPacketMaker() {
        return packetMaker;
    }

    public void setPacketMaker(PacketMaker packetMaker) {
        this.packetMaker = packetMaker;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public Socket getSocketForCommunitation() {
        return socketForCommunitation;
    }

    public void setSocketForCommunitation(Socket socketForCommunitation) {
        this.socketForCommunitation = socketForCommunitation;
    }

    public CommunicationWithServer() {
        try {
            createConnection();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createConnection() throws IOException {
        socketForCommunitation = new Socket("hermes.plusplus.rs", 4000);
    }

    public byte getByteFromServer() throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        return in.readByte();
    }

    public void importByte(byte[] bytes, int start) throws IOException {
        for (int i = start; i < bytes.length; i++) {
            bytes[i] = getByteFromServer();
        }
    }

    private void getBytesFromStream() throws IOException {
        byteHandler = new ByteHandler();
        importByte(byteHandler.getHeader(), 0);
        switch (Convertor.byteArraytoIntLE(byteHandler.getHeader())) {
            case 1:
                byteHandler.setFull(16);
                break;
            case 2:
                byteHandler.setFull(12);
                break;
            default:
                byteHandler.setFull(150);
                break;
        }
        byteHandler.fillHeaderInFullArray();
        importByte(byteHandler.getFull(), byteHandler.getHeader().length);

    }

    private void makePacketFromBytes() {
        packetMaker = new PacketMaker(byteHandler);
        packetMaker.createPacket();
    }

    public Packet getPacketFromServer() throws IOException {
        getBytesFromStream();
        makePacketFromBytes();
        return packetMaker.getPacket();
    }

    public void bringPacketBackToServer(Packet packet) throws IOException {
        out = new DataOutputStream(socketForCommunitation.getOutputStream());
        out.write(packet.getPacketArray());
        System.out.println("Packet with id:" + packet.getID() + " bring back to server.");
    }

    public void sendMessageToServer(Packet packet) throws IOException {
        out = new DataOutputStream(socketForCommunitation.getOutputStream());
        out.writeBytes("Packet with id:" + packet.getID() + " has expired.");
        System.out.println("Packet with id:" + packet.getID() + " has expired.");
    }

}
