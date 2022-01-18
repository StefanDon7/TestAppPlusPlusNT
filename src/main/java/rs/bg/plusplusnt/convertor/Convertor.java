/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.convertor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Stefan
 */
public class Convertor {

    public static byte[] intToByteArrayLE(int myInteger) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
    }

    public static int byteArraytoIntLE(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

}
