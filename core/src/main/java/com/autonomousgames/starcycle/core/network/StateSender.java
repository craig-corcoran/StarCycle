package com.autonomousgames.starcycle.core.network;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.model.GameState;
import com.badlogic.gdx.Gdx;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.Vector;

public class StateSender {

    // TODO: Pass the datagramSocket for the clients to the constructor
    private Vector<NodeUDPInfo> senderSocketVector;
    private DatagramSocket receiverDatagramSocket;
    private StarCycle.Mode mode;

    public StateSender(DatagramSocket receiverDatagramSocket, Vector<NodeUDPInfo> senderSocketVector, StarCycle.Mode mode)
    {
        this.mode = mode;
        this.receiverDatagramSocket = receiverDatagramSocket;
        this.senderSocketVector = new Vector<NodeUDPInfo>();

        if (this.mode == StarCycle.Mode.kServer) {
            for (int i=0; i < senderSocketVector.size(); i++) {
                if (i > 0) {
                    this.senderSocketVector.add(senderSocketVector.get(i));
                }
            }
        }
        else {
            this.senderSocketVector.add(senderSocketVector.get(0));
        }
    }

    public void sendState(GameState state)
    {
        // Basically now push these messages into the queue
        // which is serviced by the thread that talks to a particular client
        ByteOutputStream byteOutputStream  = new ByteOutputStream();
        Serializable stateOut;
        if (mode == StarCycle.Mode.kServer) {
            stateOut = state;
        }
        else {
            stateOut = state.playerStates[StarCycle.playerNum];
        }

        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
            oos.writeObject(stateOut);
            oos.flush();
        }
        catch (IOException e)
        {
            Gdx.app.log("StarCycle", "Exception in Object Output Stream");
        }
        byte[] buf= byteOutputStream.getBytes();
        Iterator<NodeUDPInfo> it = senderSocketVector.iterator();
        while (it.hasNext())
        {
            NodeUDPInfo cUDPInfo = it.next();
            DatagramPacket statePacket = new DatagramPacket(buf, buf.length, cUDPInfo.getIp(), cUDPInfo.getPort());
            try
            {
                receiverDatagramSocket.send(statePacket);
            }
            catch (IOException e)
            {
                Gdx.app.log("StarCycle", "Exception sending UDP Packet");
            }
        }

        // DONE!!
    }
}
