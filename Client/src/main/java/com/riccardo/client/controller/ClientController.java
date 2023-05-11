package com.riccardo.client.controller;

import com.riccardo.client.model.ClientModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ClientController {

    @FXML
    public ListView listView_label;
    @FXML
    public ListView listView_mail;

    public static void initModel(ClientModel model) {

    }

    @FXML
    private void initialize() {

        String serverAddress = "localhost"; // Indirizzo del server
        int port = 4445; // Porta del server
        try {
            Socket socket = new Socket(serverAddress, port);
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
