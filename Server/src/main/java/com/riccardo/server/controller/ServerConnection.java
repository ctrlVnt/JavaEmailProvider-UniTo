package com.riccardo.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection implements Runnable{
    Socket socket = null;
    private final int port;
    private final ServerController controller;

    /**
     * Costruttore della classe.
     * @param port   porta dove resta in attesa il server
     * @param controller   controller della vista
     */
    public ServerConnection(int port, ServerController controller) {
        this.port = port;
        this.controller = controller;
    }
    @Override
    public void run() {
        controller.updateLog("Server is waiting...");
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
            e.printStackTrace();
        }finally {
            if (socket!=null)
                try {
                    controller.updateLog("Socket closure successful");
                    socket.close();
                } catch (IOException e) {
                    controller.updateLog("Socket closure ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
        }
    }
}
