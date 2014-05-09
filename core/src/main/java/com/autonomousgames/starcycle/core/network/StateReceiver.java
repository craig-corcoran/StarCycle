package com.autonomousgames.starcycle.core.network;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.model.Model;
import com.autonomousgames.starcycle.core.model.GameState;
import com.autonomousgames.starcycle.core.model.PlayerState;
import com.badlogic.gdx.Gdx;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class StateReceiver implements Runnable {

    private DatagramSocket _receiverSocket;
    private Model model;
    private StarCycle.Mode mode;

    public StateReceiver(DatagramSocket receiverSocket, StarCycle.Mode gameMode)
    {
        _receiverSocket = receiverSocket;
        this.model = null;
        this.mode = gameMode;
    }

    public void run()
    {
        while (true)
        {
            byte[] receiveData = new byte[3000000];
            byte[] sendData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            // blocking call
            try
            {
                _receiverSocket.receive(receivePacket);
            }
            catch (IOException e)
            {
                Gdx.app.log ("StarCycle","exception receiving packet from server" );
            }
            // got the data. Go ahead and convert that to a state object
            ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream ois = null;
            try
            {
                ois = new ObjectInputStream(bais);
            }
            catch (IOException e)
            {
                Gdx.app.log ("StarCycle","exception creating object input stream" );
            }
            if (mode == StarCycle.Mode.kClient){
                GameState receivedState;
                try
                {
                    receivedState = (GameState)(ois != null ? ois.readObject() : null);
                    if (model != null & receivedState != null) {
                        model.setState(receivedState);
                    }
                }

                catch (IOException e)
                {
                    Gdx.app.log ("StarCycle", "exception receiving state info from server" );
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                // State conversion complete
            }
            else {
                PlayerState receivedState;
                try {
                    receivedState = (PlayerState)(ois != null ? ois.readObject() : null);
                    if(model != null & receivedState != null) {
                        model.setPlayerState(receivedState);
                        // Gdx.app.log("StateReceiver", "setting player state");
                    }
                }

                catch (IOException e)
                {
                    Gdx.app.log("StarCycle", "exception receiving state info from server");
                } catch (ClassNotFoundException e) {
                    Gdx.app.log("StateReceiver", e.getLocalizedMessage());
                }
                // State conversion complete
            }
        }
    }

    public void setModel (Model model)
    {
        this.model = model;
    }
}
