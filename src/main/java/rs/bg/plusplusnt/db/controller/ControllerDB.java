/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.db.controller;

import java.util.List;
import rs.bg.plusplusnt.db.MySQLBrocker;
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.db.DBService;

/**
 *
 * @author Stefan
 */
public class ControllerDB {

    private final static ControllerDB instance = new ControllerDB();
    private DBService mySqlDbBrocker;

    private ControllerDB() {
        mySqlDbBrocker = new MySQLBrocker();
    }

    public static ControllerDB getInstance() {
        return instance;
    }

    public List<Packet> getAll() {
        return mySqlDbBrocker.getAll();
    }

    public void savePacket(Packet packet) {
        mySqlDbBrocker.savePacket(packet);
    }

    public void deletePacket(Packet packet) {
        mySqlDbBrocker.deletePacket(packet);
    }

}
