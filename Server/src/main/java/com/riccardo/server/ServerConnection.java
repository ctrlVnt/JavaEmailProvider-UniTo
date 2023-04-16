package com.riccardo.server;
import com.riccardo.server.controller.ServerController;

import java.io.*;
import java.net.*;

public class ServerConnection implements Runnable {

    private final ServerSocket server;

    private final ServerController controller;

    //costruttore del server connection
    public ServerConnection(final int port, ServerController controller) throws IOException {

        this.server = new ServerSocket(port);
        this.controller = controller;

    }

    //metodo per accettare le richieste
    public Socket waitClient() throws IOException {
        return server.accept();
    }

    //chiudere la connessione
    public void closeConnection() throws IOException {
        server.close();
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Server");
        /*try {
            serverSetUp();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        controller.updateLog("[SERVER]: START WAITING FOR CLIENTS...\n");
        while (true) {
            try {
                Socket newClient = waitClient();
                //new Thread(new HandleClient(newClient, this, controller)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}