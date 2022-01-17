package rs.bg.plusplusnt.db;

import java.util.List;
import rs.bg.plusplusnt.domen.IPacket;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefan
 */
public interface IDBPacket {
    List<IPacket> getAll();
    void savePacket(IPacket packet);
    void deletePacket(IPacket packet);
}
