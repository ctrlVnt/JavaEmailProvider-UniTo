package com.riccardo.client.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

public class ClientController {





    public static class ListLabelController extends ClientController implements Runnable {
        @FXML
        private ListView<String> listView_label;
        // Metodo per inizializzare la schermata
        @FXML
        private void initialize() {
            // Qui puoi aggiungere eventuali operazioni di inizializzazione
        }
        // Metodo per impostare la lista di stringhe da visualizzare nella ListView
        public void setListData(ObservableList<String> data) {
            listView_label.setItems(data);
        }

        @Override
        public void run() {

        }
    }





    public static class ListMailController extends ClientController implements Runnable {
        @FXML
        private ListView<String> listView_mail;
        // Metodo per inizializzare la schermata
        @FXML
        private void initialize() {
            // Qui puoi aggiungere eventuali operazioni di inizializzazione
        }
        // Metodo per impostare la lista di stringhe da visualizzare nella ListView
        public void setListData(ObservableList<String> data) {
            listView_mail.setItems(data);
        }

        @Override
        public void run() {

        }
    }





    public static class ProfileInfoController extends ClientController implements Runnable {
        @FXML
        private ImageView imageViewAvatar;
        @FXML
        private Label labelUsername;
        @FXML
        private ImageView imageViewSendMail;

        @Override
        public void run() {

        }
    /*public void initialize() {
        // qui puoi inizializzare il controller e le proprietà dei componenti della view
        imageViewAvatar.setImage(new Image("@img/avatar.png"));
        labelUsername.setText("user_name");
        imageViewSendMail.setImage(new Image("@img/sendmail.png"));
    }*/

        // eventuali metodi per la gestione di eventi sui componenti della view
    }
}
