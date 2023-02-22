package com.lionfish.network;

import java.net.ServerSocket;
public class Ping extends NetworkInterface {
    public Ping(int port) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port) ) {
            this.socket = serverSocket.accept();
        }
    }
}
