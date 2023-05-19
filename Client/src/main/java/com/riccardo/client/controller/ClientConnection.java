package com.riccardo.client.controller;

import com.riccardo.client.model.ClientModel;
import com.riccardo.client.model.Email;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientConnection implements Runnable{
    private final String mailbox;
    private ArrayList<String> receivers;
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;
    ClientModel model;
    private final String serverAddress;
    private final int port;
    private final String operation;
    private Email mail;

    /*costruttore generale*/
    public ClientConnection(ClientModel model, String user, String operation){
        this.model = model;
        this.mailbox = user;
        serverAddress = "localhost";
        port = 4445;
        this.operation = operation;
    }

    /*costruttore per eliminare la mail*/
    public ClientConnection(ClientModel model, String mail, String operation, Email deleted){
        this.model = model;
        this.mailbox = mail;
        serverAddress = "localhost";
        port = 4445;
        this.operation = operation;
        this.mail = deleted;
    }

    public ClientConnection(String mail, String operation, Email tosend, ArrayList<String> receivers){
        this.mailbox = mail;
        serverAddress = "localhost";
        port = 4445;
        this.operation = operation;
        this.mail = tosend;
        this.receivers = receivers;
    }

    /*invia al server la mail che deve eliminare dal file json*/
    /*QUESTA OPERAZIONE DEVE USARE SYNCHRO*/
    private boolean deleteComunication(String host, int port){
        try {
            Socket socket = new Socket(host, port);

            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                outputStream.writeObject("deleteConnection");
                outputStream.flush();

                outputStream.writeObject(mailbox);
                outputStream.flush();

                outputStream.writeObject(mail);
                outputStream.flush();

                return true;
            } finally {
                closeConnections();
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }

    private boolean sendComunication(String host, int port) {
        try {
            Socket socket = new Socket(host, port);

            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                outputStream.writeObject("sendConnection");
                outputStream.flush();

                outputStream.writeObject(mailbox);
                outputStream.flush();

                outputStream.writeObject(receivers);
                outputStream.flush();

                outputStream.writeObject(mail);
                outputStream.flush();

                return true;
            } finally {
                closeConnections();
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }

    /*chiude le connessini*/
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

        if(Objects.equals(operation, "deleteMail"))
        {
            boolean success = false;
            while (!success) {
                success = deleteComunication(serverAddress, port);
                if (success) {
                    continue;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else if(Objects.equals(operation, "sendMail"))
        {
            boolean success = false;
            while (!success) {
                success = sendComunication(serverAddress, port);
                if (success) {
                    continue;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else if(Objects.equals(operation, "checkNewMails"))
        {
            while (true) {

                checkComunication(serverAddress, port);

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkComunication(String host, int port) {
        try {
            Socket socket = new Socket(host, port);

            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeObject("checkComunication");
                outputStream.flush();

                outputStream.writeObject(mailbox);
                outputStream.flush();

                outputStream.writeObject(model.getInboxNumber());
                outputStream.flush();

                List<Email> emails = (List<Email>) inputStream.readObject();

                Platform.runLater(() -> {
                    if (emails != null && emails.size() > 0) {
                        for (Email s : emails) {
                            model.addInboxContent(s);
                        }
                    }
                });

            } finally {
                closeConnections();
                socket.close();
            }
        } catch (IOException | RuntimeException | ClassNotFoundException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
