package com.riccardo.server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerConnection implements Runnable{

    Socket socket = null;
    ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;

    private final int port;

    public ServerConnection(int port) {
        this.port = port;
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from " + clientSocket.getInetAddress().getHostAddress());
                // Qui puoi gestire la connessione del client, ad esempio creando un nuovo thread per ogni connessione
            }
        } catch (IOException e) {
            System.err.println("Server connection error: " + e.getMessage());
        }
    }
}
