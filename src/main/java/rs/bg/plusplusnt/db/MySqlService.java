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
import rs.bg.plusplusnt.domen.Packet;
import rs.bg.plusplusnt.domen.Type;
import rs.bg.plusplusnt.filereader.SettingsLoader;

/**
 *
 * @author Stefan
 */
public final class MySqlService implements DBService {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public MySqlService() {
        loadDriver();
        openConnection();
    }

    public void loadDriver() {
        try {
            Class.forName(SettingsLoader.getInstance().getDatabaseProperty("driver"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySqlService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void openConnection() {
        try {
            String dbUrl = SettingsLoader.getInstance().getDatabaseProperty("url");
            String dbUser = SettingsLoader.getInstance().getDatabaseProperty("user");
            String dbPassword = SettingsLoader.getInstance().getDatabaseProperty("password");
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(MySqlService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Packet> getAll() {
        String query = "select * from packet";
        List<Packet> list = new ArrayList();
        try {
            Statement statement;
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                int length = rs.getInt("lengthPacket");
                int packetID = rs.getInt("packetid");
                int delay = rs.getInt("delay");
                long packetExpiration = rs.getLong("packetExpiration");
                Packet packet = createPacket(packetID, length, id, delay, packetExpiration);
                list.add(packet);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySqlService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public void savePacket(Packet packet) {
        String query = "insert into packet (id, lengthPacket, packetid, delay, packetExpiration) values (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, packet.getID());
            preparedStatement.setInt(2, packet.getLength());
            preparedStatement.setInt(3, packet.getPacketID());
            preparedStatement.setInt(4, packet.getDelay());
            preparedStatement.setLong(5, packet.getPacketExpiration());
            preparedStatement.execute();
            commit();
        } catch (SQLException ex) {
            rollback();
        }
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void deletePacket(Packet packet) {
        String query = "delete from packet where id = ?";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, packet.getID());
            preparedStatement.execute();
            commit();
            System.out.println("Packet with id:" + packet.getID() + " has been deleted from the database!");
        } catch (SQLException ex) {
            rollback();
        }

    }

    private Packet createPacket(int packetID, int length, int id, int delay, long packetExpiration) {
        Packet packet = new Packet(packetID, length, id, delay, packetExpiration);
        switch (packetID) {
            case 1:
                packet.setType(Type.Dummy);
                break;
            case 2:
                packet.setType(Type.Cancel);
                break;
            default:
                break;
        }
        packet.setPacketArray(packet.createBytePacket());
        System.out.println(packet.toString());
        return packet;
    }

}
