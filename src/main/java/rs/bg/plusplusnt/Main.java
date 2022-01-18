/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt;

import java.io.IOException;
import rs.bg.plusplusnt.communication.thread.CommunicationWithServerThread;
import rs.bg.plusplusnt.threadpool.ChargerThreadPool;

/**
 *
 * @author Stefan
 */
public class Main {

    public static void main(String[] args) throws IOException {
        CommunicationWithServerThread.getInstance().getCommunicationService().checkUnsendPacketFromDatabase();
        CommunicationWithServerThread.getInstance().startThread();
        ChargerThreadPool.getInstance().startTread();
    }
}
