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
public class Dummy implements IPacket {

    private byte[] packetArray;
    private int packetID;
    private int length;
    private int id;
    private int delay;
    private long packetExpiration;
    private Type type;

    @Override
    public byte[] getPacketArray() {
        return packetArray;
    }

    @Override
    public void setPacketArray(byte[] packetArray) {
        this.packetArray = packetArray;
    }

    @Override
    public int getPacketID() {
        return packetID;
    }

    @Override
    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public long getPacketExpiration() {
        return packetExpiration;
    }

    @Override
    public void setPacketExpiration(long packetExpiration) {
        this.packetExpiration = packetExpiration;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < packetArray.length; i++) {
            if (i % 4 == 0) {
                stringBuilder.append('\t');
            }
            stringBuilder.append(packetArray[i] + " ");
        }
        return stringBuilder.toString();
    }

    @Override
    public long createPacketExpiration(int delay) {
        return System.currentTimeMillis() + delay * 1000;
    }

    @Override
    public byte[] createBytePacket() {
        byte[] bytes = new byte[length];
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        buff.put(Convertor.intToByteArrayLE(packetID));
        buff.put(Convertor.intToByteArrayLE(length));
        buff.put(Convertor.intToByteArrayLE(id));
        buff.put(Convertor.intToByteArrayLE(delay));
        return bytes;
    }

    @Override
    public boolean hasExpired() {
        System.out.println("*****" + System.currentTimeMillis() + "*****");
        System.out.println("*****" + packetExpiration + "*****");
        return System.currentTimeMillis() > packetExpiration;
    }

}
