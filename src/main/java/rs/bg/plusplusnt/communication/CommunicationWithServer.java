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
import rs.bg.plusplusnt.bytehandler.PacketMaker;
import rs.bg.plusplusnt.domen.Packet;


/**
 *
 * @author Stefan
 */
public class CommunicationWithServer {


    private PacketMaker packetMaker=new PacketMaker();
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
       // socketForCommunitation = new Socket("localhost", 8999);
    }

    public void getBytesArrayFromServer(byte[] bytes) throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        in.read(bytes);
    }

    public byte getByteFromServer() throws IOException {
        in = new DataInputStream(socketForCommunitation.getInputStream());
        return in.readByte();
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
