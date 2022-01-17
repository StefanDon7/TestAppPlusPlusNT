/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.bytehandler;

import java.io.IOException;
import java.util.Arrays;
import rs.bg.plusplusnt.communication.thread.CommunicationWithServerThread;
import rs.bg.plusplusnt.convertor.Convertor;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.domen.Type;
import rs.bg.plusplusnt.threadpool.ThreadPoolExecutorThread;

/**
 *
 * @author Stefan
 */
public class PacketMaker {

    private Packet packet;
    private ByteHandler byteHandler=new ByteHandler();

    public PacketMaker() {
    }

    public PacketMaker(Packet packet, ByteHandler byteHandler) {
        this.packet = packet;
        this.byteHandler = byteHandler;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public ByteHandler getByteHandler() {
        return byteHandler;
    }

    public void setByteHandler(ByteHandler byteHandler) {
        this.byteHandler = byteHandler;
    }

    public void fillFull() throws IOException {
        System.arraycopy(byteHandler.getHeader(), 0, byteHandler.getFull(), 0, byteHandler.getHeader().length);
        for (int i = byteHandler.getHeader().length; i < byteHandler.getFull().length; i++) {
            byteHandler.getFull()[i] = CommunicationWithServerThread.getInstance().getCommunicationWithServer().getByteFromServer();
        }
    }

    public byte[] copyByte(byte[] array, int a, int b) {
        byte[] returnByteArray = new byte[4];
        returnByteArray = Arrays.copyOfRange(array, a, b);
        return returnByteArray;
    }

    public void getPacketFromServer() throws IOException {
        allocationBuffer();
        setAllAtributes();
        ControllerDB.getInstance().savePacket(getPacket());
        ThreadPoolExecutorThread.getInstance().getThreadPoolExecutor().addToQueue(getPacket());
    }

    public void allocationBuffer() throws IOException {
        CommunicationWithServerThread.getInstance().getCommunicationWithServer().getBytesArrayFromServer(byteHandler.getHeader());
        int packetID = Convertor.byteArraytoIntLE(byteHandler.getHeader());
        switch (packetID) {
            case 1:
                System.out.println("*****Dummy paket*****");
                byteHandler.setFull(16);
                packet = new Packet();
                packet.setType(Type.Dummy);
                packet.setPacketID(packetID);
                break;
            case 2:
                System.out.println("*****Cancel paket*****");
                byteHandler.setFull(12);
                packet = new Packet();
                packet.setType(Type.Cancel);
                packet.setPacketID(packetID);
                break;
            default:
        }
        fillFull();
    }

    public void setAllAtributes() {
        packet.setPacketArray(byteHandler.getFull());
        packet.setLength(Convertor.byteArraytoIntLE(copyByte(byteHandler.getFull(), 4, 8)));
        packet.setID(Convertor.byteArraytoIntLE(copyByte(byteHandler.getFull(), 8, 12)));
        switch (packet.getType()) {
            case Dummy:
                packet.setDelay(Convertor.byteArraytoIntLE(copyByte(byteHandler.getFull(), 12, 16)));
                packet.setPacketExpiration(packet.createPacketExpiration(packet.getDelay()));
                break;
            case Cancel:
                packet.setDelay(0);
                packet.setPacketExpiration(packet.createPacketExpiration(packet.getDelay()));
                break;
            default:
        }
    }
}
