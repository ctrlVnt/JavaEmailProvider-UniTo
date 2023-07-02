package com.riccardo.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.net.InetAddress.getLocalHost;

public class ServerConnection implements Runnable{

    ServerSocket serverSocket = null;
    private final int port;
    private final ServerController controller;
    private ExecutorService executorService;

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
        try {
            controller.updateLog("Server "+ getLocalHost() + " is waiting on port "+ port + "...");
            executorService = Executors.newFixedThreadPool(8);
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new Thread(new ServerOperations(clientSocket, controller)));
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
            if (executorService != null) {
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}