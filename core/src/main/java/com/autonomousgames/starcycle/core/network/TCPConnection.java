package com.autonomousgames.starcycle.core.network;

import com.badlogic.gdx.Gdx;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class TCPConnection extends Thread{
    UUID clientUUID;
    ObjectOutput objectOut;
    ObjectInput objectIn;
    long lastMsgTime;
    int timeOut = 2000;
    boolean timedOut = false;
    public static HashMap<Integer, UUID> connectedClientIds = new HashMap<Integer, UUID>();

    public TCPConnection(Socket client){

        try {
            OutputStream outputStream = client.getOutputStream();
            objectOut = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream inputStream = client.getInputStream();
            objectIn = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // GET client UUID
        start();
    }

    public void run(){
        while (!timedOut){
            try {
                String input;
                String response;
                Object o;
                while ((o = objectIn.readObject()) != null){//(in.ready() && (input = in.readLine()) != null){
                    if (o instanceof UUID){
                        clientUUID = (UUID) o;
                        System.out.println("New client connection: " + clientUUID);
                        if (connectedClientIds.get(0) == null) {
                            connectedClientIds.put(0, clientUUID);
                            response = "0";
                        }
                        else if (connectedClientIds.get(1) == null) {
                            connectedClientIds.put(1, clientUUID);
                            response = "1";
                        }
                        else {
                            Gdx.app.log("TCPConnection", "too many clients trying to connect");
                            response = "you cant connect right now";
                        }
                    } else if (o instanceof String){
                        response = handleTCPMessage((String)o);
                    } else response = "received";

                    lastMsgTime = System.currentTimeMillis();
                    //System.out.println("Last time now set = " + lastMsgTime);
                    objectOut.writeObject(response);
                }
            } catch (IOException e) {
                // TODO: remove the existing client from the queue, if there's only one
                System.out.println("Connection broken");
                e.printStackTrace();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Compute lastMsgTime based on frames?
            if (lastMsgTime != 0 && System.currentTimeMillis() - lastMsgTime > timeOut) {
                timedOut = true;
            }
        }
        System.out.println("Timed out - server");
        //TODO:  HANDLER FOR TIMEOUTS - SERVER SIDE
    }
    public void timeoutHandler(){
        // The client associated with this connection has timed out
        // Report this state to the other client
    }

    private String handleTCPMessage(String msg){
        if (msg.equals("heartbeat")){
            return "received";
        }else if (msg.equals("???")){
            return "response";
        }else{
            return "response";
        }
    }
}
