/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.makers;

import rs.bg.plusplusnt.convertor.Convertor;
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.domen.Type;

/**
 *
 * @author Stefan
 */
public class PacketMaker {

    private final ByteHandler byteHandler;
    private Packet packet;

    public PacketMaker(ByteHandler byteHandler) {
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

    private void determinePacket() {
        int packetID = Convertor.byteArraytoIntLE(byteHandler.getHeader());
        packet = new Packet();
        packet.setPacketID(packetID);
        switch (packet.getPacketID()) {
            case 1:
                packet.setType(Type.Dummy);
                break;
            case 2:
                packet = new Packet();
                packet.setType(Type.Cancel);
                break;
            default:
        }
    }

    private void setAllAttributes() {
        packet.setPacketArray(byteHandler.getFull());
        packet.setLength(Convertor.byteArraytoIntLE(byteHandler.cutFromArray(4, 8)));
        packet.setID(Convertor.byteArraytoIntLE(byteHandler.cutFromArray(8, 12)));
        switch (packet.getType()) {
            case Dummy:
                packet.setDelay(Convertor.byteArraytoIntLE(byteHandler.cutFromArray(12, 16)));
                packet.setPacketExpiration(packet.createPacketExpiration(packet.getDelay()));
                break;
            case Cancel:
                packet.setDelay(0);
                packet.setPacketExpiration(packet.createPacketExpiration(packet.getDelay()));
                break;
            default:
        }
    }

    public void createPacket() {
        determinePacket();
        setAllAttributes();
    }
}
