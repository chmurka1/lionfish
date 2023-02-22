package com.lionfish.network;

import com.lionfish.util.ErrorDialog;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Pong extends NetworkInterface {
    @Override
    protected Void call() {
        try{
            try(Socket socket = new Socket(address, port)) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                out.writeObject(null);
                outQueue.put(in.readObject());

                while(true) {
                    out.writeObject(inQueue.take());
                    outQueue.put(in.readObject());
                }
            }
        }
        catch(InterruptedException ignored)
        {
        }
        catch(Exception ignored) {
            cancel();
        }
        return null;
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        new ErrorDialog("Fatal error: connection refused");
        System.exit(0);
    }

    public Pong(String address, int port) {
        super();
        this.address = address;
        this.port = port;
    }
}
