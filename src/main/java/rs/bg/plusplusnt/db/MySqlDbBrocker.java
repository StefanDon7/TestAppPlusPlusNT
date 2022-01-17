/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.bg.plusplusnt.domen.Cancel;
import rs.bg.plusplusnt.domen.Dummy;
import rs.bg.plusplusnt.domen.IPacket;

/**
 *
 * @author Stefan
 */
public class MySqlDbBrocker implements IDBPacket {

    private Connection connection;

    public MySqlDbBrocker() {
        loadDriver();
        openConnection();
    }

    public void loadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/plusplusnt", "root", "");
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<IPacket> getAll() {
        String query = "select * from packet";
        List<IPacket> list = new ArrayList();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                int length = rs.getInt("lengthPacket");
                int packetID = rs.getInt("packetid");
                int delay = rs.getInt("delay");
                long packetExpiration = rs.getLong("packetExpiration");
                IPacket packet = createPacket(packetID, length, id, delay, packetExpiration);
                list.add(packet);

            }
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public void savePacket(IPacket packet) {
        String query = "insert into packet (id, lengthPacket, packetid, delay, packetExpiration) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            preparedStatement.setInt(1, packet.getID());
            preparedStatement.setInt(2, packet.getLength());
            preparedStatement.setInt(3, packet.getPacketID());
            preparedStatement.setInt(4, packet.getDelay());
            preparedStatement.setLong(5, packet.getPacketExpiration());
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            preparedStatement.execute();
            commit();
        } catch (SQLException ex) {
            rollback();
        }
    }

    @Override
    public void deletePacket(IPacket packet) {
        String query = "delete from packet where id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            preparedStatement.setInt(1, packet.getID());
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDbBrocker.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            preparedStatement.execute();
            commit();
            System.out.println("Packet with id:" + packet.getID() + " has been deleted from the database!");
        } catch (SQLException ex) {
            rollback();
        }

    }

    private IPacket createPacket(int packetID, int length, int id, int delay, long packetExpiration) {
        IPacket packet = null;
        switch (packetID) {
            case 1:
                packet = new Dummy();
                break;
            case 2:
                packet = new Cancel();
                break;

            default:
                break;

        }
        packet.setID(id);
        packet.setLength(length);
        packet.setPacketID(packetID);
        packet.setDelay(delay);
        packet.setPacketExpiration(packetExpiration);
        packet.setPacketArray(packet.createBytePacket());
        System.out.println(packet.toString());
        return packet;
    }

}
