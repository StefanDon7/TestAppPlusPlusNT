/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.communication;

import rs.bg.plusplusnt.domen.Packet;

/**
 *
 * @author Stefan
 */
public interface CommunicationService {

    Packet getPacketFromServer();
    void sendMessageToServer(Packet packet);
    void bringPacketBackToServer(Packet packet);
}
