package com.riccardo.client.controller;

import com.riccardo.client.model.ClientModel;
import com.riccardo.client.model.Email;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ClientConnection implements Runnable{

    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    ClientModel model;

    public ClientConnection(ClientModel model){
        this.model = model;
    }
    private boolean tryCommunication(String host, int port){
        try {
            Socket socket = new Socket(host, port);

            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
                /*outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();

                String user = "Katherine Johnson";
                outputStream.writeUTF(user);
                outputStream.flush();*/

                List<Email> emails = (List<Email>) inputStream.readObject();

                if (emails != null && emails.size() > 0) {
                    for (Email s : emails) {
                        model.addInboxContent(s);
                    }
                }

                return true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                closeConnections();
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }

    private void closeConnections() {
        try {
            if(inputStream != null) {
                inputStream.close();
            }

            if(outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        /*inizio connessione server*/
        String serverAddress = "localhost";
        int port = 4445;
        boolean success = false;

        while(!success) {

            success = tryCommunication(serverAddress, port);

            if(success) {
                continue;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
