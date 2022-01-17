/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.db.controller;

import java.util.List;
import rs.bg.plusplusnt.db.IDBPacket;
import rs.bg.plusplusnt.db.MySqlDbBrocker;
import rs.bg.plusplusnt.domen.IPacket;

/**
 *
 * @author Stefan
 */
public class ControllerDB {

    private final static ControllerDB instance = new ControllerDB();
    IDBPacket mySqlDbBrocker;

    private ControllerDB() {
        mySqlDbBrocker = new MySqlDbBrocker();
    }

    public static ControllerDB getInstance() {
        return instance;
    }

    public List<IPacket> getAll() {
        return mySqlDbBrocker.getAll();
    }

    public void savePacket(IPacket packet) {
        mySqlDbBrocker.savePacket(packet);
    }

    public void deletePacket(IPacket packet) {
        mySqlDbBrocker.deletePacket(packet);
    }

}
