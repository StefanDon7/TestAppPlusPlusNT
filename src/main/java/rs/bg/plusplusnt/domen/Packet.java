/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.domen;

import java.nio.ByteBuffer;
import rs.bg.plusplusnt.convertor.Convertor;

/**
 *
 * @author Stefan
 */
public class Packet {

    private byte[] packetArray;
    private int packetID;
    private int length;
    private int id;
    private int delay;
    private long packetExpiration;
    private Type type;

    public Packet() {
    }

    public Packet(int packetID, int length, int id, int delay, long packetExpiration) {
        this.packetID = packetID;
        this.length = length;
        this.id = id;
        this.delay = delay;
        this.packetExpiration = packetExpiration;
    }

    public byte[] getPacketArray() {
        return packetArray;
    }

    public void setPacketArray(byte[] packetArray) {
        this.packetArray = packetArray;
    }

    public int getPacketID() {
        return packetID;
    }

    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public long getPacketExpiration() {
        return packetExpiration;
    }

    public void setPacketExpiration(long packetExpiration) {
        this.packetExpiration = packetExpiration;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (int i = 0; i < packetArray.length; i++) {
            if (i % 4 == 0 && i != 0) {
                stringBuilder.append(']');
                stringBuilder.append('\t');
                stringBuilder.append('[');
            }
            stringBuilder.append(packetArray[i]).append(" ");
        }
        stringBuilder.append(']');
        stringBuilder.append(" ID:").append(id);
        stringBuilder.append(" Expiration time: ").append(packetExpiration);
        stringBuilder.append(" Current time: ").append(System.currentTimeMillis());
        stringBuilder.append(" Type: ").append(type);
        return stringBuilder.toString();
    }

    public long createPacketExpiration(int delay) {
        return System.currentTimeMillis() + delay * 1000;
    }

    public byte[] createBytePacket() {
        byte[] bytes = new byte[length];
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        buff.put(Convertor.intToByteArrayLE(packetID));
        buff.put(Convertor.intToByteArrayLE(length));
        buff.put(Convertor.intToByteArrayLE(id));
        buff.put(Convertor.intToByteArrayLE(delay));
        return bytes;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() > packetExpiration;
    }

    public long getTimeToExecute() {
        return packetExpiration - System.currentTimeMillis();
    }

}
