package com.autonomousgames.starcycle.core.network;

import java.net.InetAddress;

public class NodeUDPInfo
{
    private InetAddress inetAddress;
    private int port;

    public NodeUDPInfo(InetAddress ip, int port)
    {
        inetAddress = ip;
        this.port = port;
    }

    public InetAddress getIp()
    {
        return inetAddress;
    }

    public int getPort ()
    {
        return port;
    }
}
