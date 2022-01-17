/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.domen;

/**
 *
 * @author Stefan
 */
public interface IPacket {

    void setPacketArray(byte[] packetArray);

    byte[] getPacketArray();

    void setPacketID(int packetID);

    int getPacketID();

    void setLength(int length);

    int getLength();

    void setID(int id);

    int getID();

    void setDelay(int delay);

    int getDelay();

    void setPacketExpiration(long packetExpiration);

    long getPacketExpiration();

    void setType(Type type);

    Type getType();

    long createPacketExpiration(int delay);

    byte[] createBytePacket();

    boolean hasExpired();
}
