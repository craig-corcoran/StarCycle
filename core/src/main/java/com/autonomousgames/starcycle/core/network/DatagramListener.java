package com.autonomousgames.starcycle.core.network;

import java.net.*;
import java.io.*;

public class DatagramListener extends Thread{
    DatagramPacket datapacket, returnpacket;
    int port;
    int len = 1024;

    public DatagramListener(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket datasocket = new DatagramSocket(port);
            byte[] buf = new byte[len];
            while (true) {
                String response;
                try {
                    datapacket = new DatagramPacket(buf, buf.length);
                    datasocket.receive(datapacket);
                    String clientMessage = new String(datapacket.getData(), 0, datapacket.getLength());
                    response = "I heard you! You said: " + clientMessage;

                    returnpacket = new DatagramPacket(
                            response.getBytes(),
                            response.getBytes().length,
                            datapacket.getAddress(),
                            datapacket.getPort());
                    datasocket.send(returnpacket);
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        } catch (SocketException se) {
            System.err.println(se);
        }
    }
}
