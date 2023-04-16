package com.riccardo.server.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 *
 * @author Riccardo Venturini
 */
public class ServerController implements Runnable  {

    @FXML
    private Text server_text;

    @FXML
    private TextArea log_text;

    @FXML
    private ImageView refresh;

    // Metodo per inizializzare la schermata del server
    @FXML
    private void initialize() {
        // Qui puoi aggiungere eventuali operazioni di inizializzazione
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

    @Override
    public void run() {

    }
}