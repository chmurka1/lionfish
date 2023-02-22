package com.lionfish.network;

import javafx.concurrent.Task;
import java.util.concurrent.SynchronousQueue;

public abstract class NetworkInterface extends Task<Void> {
    protected String address;
    protected int port;
    protected SynchronousQueue<Object> inQueue, outQueue;

    protected boolean refused;

    public NetworkInterface() {
        refused = false;
        inQueue = new SynchronousQueue<>();
        outQueue = new SynchronousQueue<>();
    }
    public Object getObject() throws Exception {
        if(refused) throw new Exception();
        try {
            return outQueue.take();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setObject(Object obj) {
        try {
            inQueue.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
