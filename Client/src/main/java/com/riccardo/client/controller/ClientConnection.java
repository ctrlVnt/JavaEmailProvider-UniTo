package com.riccardo.client.controller;

import com.riccardo.client.Client;
import com.riccardo.client.model.ClientModel;
import com.riccardo.client.model.Email;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
    private boolean firstconnection = true;

    /**
     * Costruttore della classe.
     * @param model   model della mail che sto utilizzando
     * @param user   indirizzo email principale
     * @param operation   operazione che voglio eseguire
     */
    public ClientConnection(ClientModel model, String user, String operation){
        this.model = model;
        this.mailbox = user;
        serverAddress = "localhost";
        port = 4445;
        this.operation = operation;
    }

    /**
     * Costruttore della classe.
     * @param model   model della mail che sto utilizzando
     * @param mail   indirizzo email principale
     * @param operation   operazione che voglio eseguire
     * @param deleted   mail da eliminare
     */
    public ClientConnection(ClientModel model, String mail, String operation, Email deleted){
        this.model = model;
        this.mailbox = mail;
        serverAddress = "localhost";
        port = 4445;
        this.operation = operation;
        this.mail = deleted;
    }

    /**
     * Costruttore della classe.
     * @param mail   indirizzo email principale
     * @param operation   operazione che voglio eseguire
     * @param tosend   mail da spedire
     * @param receivers lista destinatari
     */
    public ClientConnection(String mail, String operation, Email tosend, ArrayList<String> receivers){
        this.mailbox = mail;
        serverAddress = "localhost";
        port = 4445;
        this.operation = operation;
        this.mail = tosend;
        this.receivers = receivers;
    }

    /**
     * Invia una richiesta al server per eliminare una mail, e la elimina in locale.
     * @param host   il nome dell'host
     * @param port   porta alla quale connettersi
     * @return TRUE se l'operazione va a buon fine, FALSE altrimenti
     */
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

    /**
     * Invia una richiesta al server per inviare una mail.
     * @param host   il nome dell'host
     * @param port   porta alla quale connettersi
     * @return TRUE se l'operazione va a buon fine, FALSE altrimenti
     */
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

    /**
     * Chiude le connessioni aperte per la comunicazione di informazioni.
     */
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

    /**
     * Invia una richiesta al server per verificare se sono arrivate nuove mail.
     * @param host   il nome dell'host
     * @param port   porta alla quale connettersi
     */
    private void checkComunication(String host, int port) {
        try {
            Socket socket = new Socket(host, port);

            try {
                model.setAllertConnection("");
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
                        if(!firstconnection) {
                            buildNotif(emails.size());
                        }else if(firstconnection){
                            firstconnection = false;
                        }
                        for (Email s : emails) {
                            model.addInboxContent(s);
                        }
                    }
                    firstconnection = false;
                });

            } finally {
                closeConnections();
                socket.close();
            }
        } catch (IOException | RuntimeException | ClassNotFoundException e) {
            model.setAllertConnection("connection failed: waiting for connection...");
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    /**
     * Costruisce una finestra di notifiche per le nuove mail arrivate.
     * @param nmail numero di mail arrivate
     */
    private void buildNotif(int nmail){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("new_mail.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 200);

            NotifController controller = fxmlLoader.getController();
            controller.setNmail(nmail);

            stage.setTitle(mailbox + ": new mail");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    /**
     * Segnala la prima connessione effettuata.
     * @param host   il nome dell'host
     * @param port   porta alla quale connettersi
     * @return TRUE se l'operazione va a buon fine, FALSE altrimenti
     */
    private boolean fistConnection(String host, int port) {
        try {
            Socket socket = new Socket(host, port);

            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                outputStream.writeObject("firstConnection");
                outputStream.flush();

                outputStream.writeObject(mailbox);
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

    /**
     * Segnala che il client si è disconnesso
     * @param host   il nome dell'host
     * @param port   porta alla quale connettersi
     * @return TRUE se l'operazione va a buon fine, FALSE altrimenti
     */
    private boolean lastConnection(String host, int port) {
        try {
            Socket socket = new Socket(host, port);

            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                outputStream.writeObject("lastConnection");
                outputStream.flush();

                outputStream.writeObject(mailbox);
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

    @Override
    public void run() {
        boolean success = false;
        if(Objects.equals(operation, "deleteMail"))
        {
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
        }else if(Objects.equals(operation, "firstConnection"))
        {
            while (!success) {
                success = fistConnection(serverAddress, port);
                if (success) {
                    continue;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else if(Objects.equals(operation, "lastConnection"))
        {
            while (!success) {
                success = lastConnection(serverAddress, port);
                if (success) {
                    continue;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
