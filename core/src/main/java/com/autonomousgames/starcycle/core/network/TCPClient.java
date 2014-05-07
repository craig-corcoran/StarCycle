package com.autonomousgames.starcycle.core.network;

import com.autonomousgames.starcycle.core.StarCycle;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class TCPClient extends Thread implements Runnable{
    Socket connectionToServer;
    long lastMsgTime;
    private final long timeOut = 2000;
    private boolean timedOut = false;
    private int port;
    UUID myUUID = UUID.randomUUID(); //TODO:  NEED TO CHANGE THIS
    ObjectInput objectIn = null;
    ObjectOutput objectOut = null;
    public TCPClient(InetAddress ia, int port){
        this.port = port;
        try {
            connectionToServer = new Socket(ia, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }


    @Override
    public void run(){
        try {
            objectIn = new ObjectInputStream(connectionToServer.getInputStream());
            objectOut = new ObjectOutputStream(connectionToServer.getOutputStream());
            TCPClientListener listener = new TCPClientListener(objectIn, this);
            listener.start();
        } catch (IOException e) {
            e.printStackTrace(); // Try again?  Print an error message/disconnect?
        }
        if (objectOut != null) {
            try {
                sendTCPMessageToServer(myUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Connected to server on port " + connectionToServer.getPort());
        while (!timedOut){
            try {
                sendTCPMessageToServer("heartbeat");
                Thread.sleep(1000);
                if (lastMsgTime > 0 && (System.currentTimeMillis() - lastMsgTime > timeOut)) {
                    System.out.println("setting timedout to true - currentTime = " + System.currentTimeMillis() + ", last time = " + lastMsgTime);
                    timedOut = true;
                }

            } catch (IOException e) {
                System.out.println("Connection broken");
                // Display a "Disconnected from server" message - the server will time out and handle the other player
                e.printStackTrace();
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Timed Out");
        //TODO: HANDLER FOR TIMEOUTS - CLIENT SIDE
    }

    private void setMessageTime(String msg) {
        //System.out.println("Setting message time");
        lastMsgTime = System.currentTimeMillis();
    }
    public int getPort(){
        return port;
    }
    private void sendTCPMessageToServer(Object msg) throws IOException{
        objectOut.writeObject(msg);
    }
    public void handleTCPMessage(String msg){
        if (msg.equals("other disconnected")){
            // other client disconnected
        }else if (msg.equals("you won")){
            // display win screen
            // pass a message up to the
        }else{
            // basic receive message - update timeout timer
            if (msg.equals("0")){
                StarCycle.playerNum = 0;
            }
            else if (msg.equals("1")){
                StarCycle.playerNum = 1;
            }
            setMessageTime(msg);
        }
    }
}
