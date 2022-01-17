/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt;

import java.io.IOException;
import java.util.List;
import rs.bg.plusplusnt.communication.CommunitationWithServer;
import rs.bg.plusplusnt.db.controller.ControllerDB;
import rs.bg.plusplusnt.domen.IPacket;
import rs.bg.plusplusnt.threadpool.ThreadPoolExecutor;

/**
 *
 * @author Stefan
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<IPacket> lista = ControllerDB.getInstance().getAll();
        for (IPacket iPacket : lista) {
            if (iPacket.hasExpired()) {
                CommunitationWithServer.getInstance().sendMessageToServer(iPacket);
                ControllerDB.getInstance().deletePacket(iPacket);
            } else {
                CommunitationWithServer.getInstance().addToQueue(iPacket);
            }
        }
        CommunitationWithServer.getInstance().start();
        ThreadPoolExecutor.getInstance().start();
    }
}
