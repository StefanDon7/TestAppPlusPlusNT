/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.bytehandler;

import java.io.IOException;
import java.util.Arrays;
import rs.bg.plusplusnt.communication.thread.CommunicationWithServerThread;


/**
 *
 * @author Stefan
 */
public class ByteHandler {

    private byte[] header = new byte[4];
    private byte[] full;

    public ByteHandler() {
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
            full[i] = CommunicationWithServerThread.getInstance().getCommunicationWithServer().getByteFromServer();
        }
    }

    public byte[] copyByte(byte[] array, int a, int b) {
        byte[] returnByteArray = new byte[4];
        returnByteArray = Arrays.copyOfRange(array, a, b);
        return returnByteArray;
    }

}
