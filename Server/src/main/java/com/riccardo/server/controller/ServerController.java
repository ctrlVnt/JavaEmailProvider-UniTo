package com.riccardo.server.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 *
 * @author Riccardo Venturini
 */
public class ServerController {

    @FXML
    private TextArea log_text;

    @FXML
    private void initialize() {
        updateLog("Server is starting...");

        Thread serverConnection = new Thread(new ServerConnection(4445, this));
        serverConnection.start();
    }

    // Metodo per aggiornare il testo del log
    public void updateLog(String text) {
        log_text.appendText(text + "\n");
    }
}