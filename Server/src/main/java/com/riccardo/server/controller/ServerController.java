package com.riccardo.server.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerController {

    @FXML
    private TextArea log_text;

    @FXML
    private void initialize() {
        updateLog("Server is starting...");

        Thread serverConnection = new Thread(new ServerConnection(4445, this));
        serverConnection.start();
    }

    /**
     * Inserisce una nuova linea di log.
     * @param text  testo da aggiungere
     */
    public void updateLog(String text) {
        log_text.appendText(text + "\n");
    }

    /**
     * Handler per terminare i thread e il programma stesso.
     */
    public void handleClose() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Termina i thread in esecuzione e il programma al chiudersi della finestra.
     * @param stage lo stage da chiudere
     */
    public void setCloseEventHandler(Stage stage) {
        stage.setOnCloseRequest((WindowEvent event) -> handleClose());
    }
}