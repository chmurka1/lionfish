package com.lionfish.network;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Pong extends NetworkInterface {
    public Pong(String address, int port) {
        try{
            this.socket = new Socket(address, port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectOutputStream.writeObject(null);
        }
        catch(Exception e)
        {
            System.out.println("Connection error: "+e.getMessage());
        }
    }
}
