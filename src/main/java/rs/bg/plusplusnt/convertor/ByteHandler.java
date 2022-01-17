/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.convertor;

import java.io.IOException;
import java.util.Arrays;
import rs.bg.plusplusnt.communication.CommunitationWithServer;
import rs.bg.plusplusnt.domen.Cancel;
import rs.bg.plusplusnt.domen.Dummy;
import rs.bg.plusplusnt.domen.IPacket;
import rs.bg.plusplusnt.domen.Type;

/**
 *
 * @author Stefan
 */
public class ByteHandler {

    private IPacket packet;
    private byte[] header = new byte[4];
    private byte[] full;

    public ByteHandler() {
    }

    public IPacket getPacket() {
        return packet;
    }

    public void setPacket(IPacket packet) {
        this.packet = packet;
    }

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getFull() {
        return full;
    }

    public void setFull(int length) {
        full = new byte[length];
    }

    public void fillFull() throws IOException {
        System.arraycopy(header, 0, full, 0, header.length);
        for (int i = header.length; i < full.length; i++) {
            full[i] = CommunitationWithServer.getInstance().getByteFromServer();
        }
    }

    public byte[] copyByte(byte[] array, int a, int b) {
        byte[] returnByteArray = new byte[4];
        returnByteArray = Arrays.copyOfRange(array, a, b);
        return returnByteArray;
    }

    public void allocationBuffer() throws IOException {
        CommunitationWithServer.getInstance().getBytesArrayFromServer(getHeader());
        int packetID = Convertor.byteArraytoIntLE(getHeader());
        switch (packetID) {
            case 1:
                System.out.println("*****Dummy paket*****");
                setFull(16);
                packet = new Dummy();
                packet.setType(Type.Dummy);
                packet.setPacketID(packetID);
                break;
            case 2:
                System.out.println("*****Cancel paket*****");
                setFull(12);
                packet = new Cancel();
                packet.setType(Type.Cancel);
                packet.setPacketID(packetID);
                break;
            default:
        }
        fillFull();
    }

    public void setAllAtributes() {
        packet.setPacketArray(getFull());
        packet.setLength(Convertor.byteArraytoIntLE(copyByte(getFull(), 4, 8)));
        packet.setID(Convertor.byteArraytoIntLE(copyByte(getFull(), 8, 12)));
        switch (packet.getType()) {
            case Dummy:
                packet.setDelay(Convertor.byteArraytoIntLE(copyByte(getFull(), 12, 16)));
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
