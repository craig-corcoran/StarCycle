package com.autonomousgames.starcycle.core.network;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerHandler extends Thread implements Runnable{
    int numConnectedClients = 0;
    int port;

    public TCPServerHandler(int port){
        this.port = port;
        Gdx.app.log("TCPServerHandler.java ", "Server listening on port " + port);
        //run();
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (numConnectedClients < 2){
                Socket client = ss.accept();
                System.out.println("Received connection " + (numConnectedClients + 1));
                TCPConnection clientConnection = new TCPConnection(client);
                numConnectedClients++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // If we're PRE-game, numClientsConnected-- and keep waiting
            // Otherwise, send a disconnection message to the other client
        }
    }
}
