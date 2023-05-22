package com.riccardo.server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerConnection implements Runnable{
    Socket socket = null;
    private final int port;
    private final ServerController controller;

    public ServerConnection(int port, ServerController controller) {
        this.port = port;
        this.controller = controller;
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread serverOperations = new Thread(new ServerOperations(clientSocket, controller));
                serverOperations.start();
            }
        } catch (IOException e) {
            controller.updateLog("Server connection error: " + e.getMessage());
            System.err.println("Server connection error: " + e.getMessage());
        }finally {
            if (socket!=null)
                try {
                    controller.updateLog("Socket closure successful");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
