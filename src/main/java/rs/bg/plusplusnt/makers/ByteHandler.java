/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.makers;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author Stefan
 */
public class ByteHandler {

    private final byte[] header = new byte[4];
    private byte[] full;

    public byte[] getHeader() {
        return header;
    }

    public byte[] getFull() {
        return full;
    }

    public void setFull(int length) {
        full = new byte[length];
        fillHeaderInFullArray();
    }

    private void fillHeaderInFullArray() {
        ByteBuffer buff = ByteBuffer.wrap(full);
        buff.put(header);
    }

    public byte[] cutFromArray(int a, int b) {
        return Arrays.copyOfRange(full, a, b);

    }

}
