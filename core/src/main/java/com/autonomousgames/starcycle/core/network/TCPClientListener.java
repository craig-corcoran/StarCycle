package com.autonomousgames.starcycle.core.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClientListener extends Thread implements Runnable{
    ObjectInput objectIn;
    TCPClient tcpClient;
    public TCPClientListener(ObjectInput objectIn, TCPClient tcpClient){
        this.objectIn = objectIn;
        this.tcpClient = tcpClient;
    }
    @Override
    public void run() {
        while (true){
            Object o;
            try{
                if ((o = objectIn.readObject()) != null){
                    if (o instanceof String){
                        String message = (String)o;
                        tcpClient.handleTCPMessage(message);
                    }
                }
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            } catch (IOException i){
                System.out.println("Disconnected");
                return;
            }
        }
    }
}
