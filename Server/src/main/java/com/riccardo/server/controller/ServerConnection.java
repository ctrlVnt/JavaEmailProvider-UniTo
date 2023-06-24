package com.riccardo.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerConnection implements Runnable{

    ServerSocket serverSocket = null;
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
            Executor executor = Executors.newFixedThreadPool(8);
            serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new Thread(new ServerOperations(clientSocket, controller)));
            }
        } catch (IOException e) {
            controller.updateLog("Server connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (serverSocket != null)
                try {
                    serverSocket.close();
                    controller.updateLog("Socket closure successful");
                } catch (IOException e) {
                    controller.updateLog("Socket closure ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
        }
    }
}