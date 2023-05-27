package com.riccardo.server.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    public void handleClose() {
        Platform.exit();
        System.exit(0);
    }
    public void setCloseEventHandler(Stage stage) {
        stage.setOnCloseRequest((WindowEvent event) -> handleClose());
    }
}