package com.autonomousgames.starcycle.java;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.network.NodeUDPInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

public class StarCycleDesktop {
    public static void main (String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Starcycle";
        cfg.useGL20 = false;
        cfg.width = 1280;
        cfg.height = 800;
        StarCycle.Mode mode = StarCycle.Mode.kClient;
        Vector<NodeUDPInfo> v = new Vector<NodeUDPInfo>();

        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equalsIgnoreCase("s"))
            {
                mode = StarCycle.Mode.kServer;
                continue;
            }
            else
            {
                if (args[i].equalsIgnoreCase("c"))
                {
                    mode = StarCycle.Mode.kClient;
                    continue;
                }

            }

            try
            {
                String[] clientAddress = args[i].split(":");
                NodeUDPInfo udpInfo;
                udpInfo = new NodeUDPInfo(InetAddress.getByName(clientAddress[0]), Integer.parseInt(clientAddress[1]));
                v.add(udpInfo);
            }
            catch (UnknownHostException e)
            {
                Gdx.app.log ("StarCycle", " Unknown host exception");
            }
        }

        new LwjglApplication(new StarCycle(mode, v), cfg);
    }
}