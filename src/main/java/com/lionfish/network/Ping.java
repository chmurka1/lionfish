package com.lionfish.network;

import com.lionfish.util.ErrorDialog;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Ping extends NetworkInterface {
    @Override
    protected Void call() {
        try{
            try(ServerSocket serverSocket = new ServerSocket(port)) {
                Socket socket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                in.readObject();
                outQueue.put(new Object());

                while(true) {
                    out.writeObject(inQueue.take());
                    outQueue.put(in.readObject());
                }
            }
        }
        catch(Exception e)
        {
            cancel();
        }
        return null;
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        //new ErrorDialog("Fatal error: connection refused");
        System.exit(0);
    }

    public Ping(int port) {
        super();
        this.port = port;
    }
}
