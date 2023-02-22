package com.lionfish.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class NetworkInterface {
    protected Socket socket;
    public Object sendRec(Object obj) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(obj);
            //out.close();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public Object rec() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
