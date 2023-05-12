package com.riccardo.server.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 *
 * @author Riccardo Venturini
 */
public class ServerController {

    @FXML
    private Text server_text;

    @FXML
    private TextArea log_text;

    @FXML
    private ImageView refresh;

    @FXML
    private void initialize() {
        updateLog("Server is starting...");

        Thread serverConnection = new Thread(new ServerConnection(4445, this));
        serverConnection.start();
    }

    // Metodo che verrà chiamato quando si clicca sull'immagine di refresh
    @FXML
    private void handleRefreshClick() {
        // Qui puoi implementare l'azione che vuoi eseguire quando si clicca sull'immagine di refresh
    }

    // Metodo per aggiornare il testo del log
    public void updateLog(String text) {
        log_text.appendText(text + "\n");
    }
}