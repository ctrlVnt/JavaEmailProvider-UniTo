package com.riccardo.client.controller;

import com.riccardo.client.Client;
import com.riccardo.client.model.ClientModel;
import com.riccardo.client.model.Email;
import com.riccardo.client.model.EmailModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClientController {

    @FXML
    private Label lblFrom;

    @FXML
    private Label lblTo;

    @FXML
    private Label lblSubject;

    @FXML
    private Label lblUsername;

    @FXML
    private TextArea txtEmailContent;

    @FXML
    private ListView<Email> lstEmails;

    private ClientModel model;
    private Email selectedEmail;
    private Email emptyEmail;

    private String user;

    @FXML
    public void initialize() throws InterruptedException {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        //istanza nuovo client
        user = "Katherine.johnson@unito.it";
        model = new ClientModel(user);

        /*thread per connessione al server*/
        Thread connection = new Thread(new ClientConnection(model, user, "firstConnection"));
        connection.start();
        /*prima di caricare le mail aspetto che si colleghi*/
        connection.join();

        selectedEmail = null;

        //binding tra lstEmails e inboxProperty
        lstEmails.itemsProperty().bind(model.inboxProperty());
        //fa showSelectedEmail quando clicco sopra, ma non è strettamente necessario, potrei settarlo
        // nel SceneBuilder l'apertura di quel metodo e sarebbe equivalente, come onDeleteButtonClick
        lstEmails.setOnMouseClicked(this::showSelectedEmail);
        //Non troppo utile
        lblUsername.textProperty().bind(model.emailAddressProperty());

        /*Mail vuota*/
        emptyEmail = new Email("", List.of(""), "", "");

        updateDetailView(emptyEmail);
    }

    /**
     * Mostra la mail selezionata nella vista
     */
    protected void showSelectedEmail(MouseEvent mouseEvent) {
        Email email = lstEmails.getSelectionModel().getSelectedItem();
        selectedEmail = email;
        updateDetailView(email);
    }

    /**
     * Aggiorna la vista con la mail selezionata
     */
    protected void updateDetailView(Email email) {
        if(email != null) {
            lblFrom.setText(email.getSender());
            lblTo.setText(String.join(", ", email.getReceivers()));
            lblSubject.setText(email.getSubject());
            txtEmailContent.setText(email.getText());
        }
    }

    /**
     * Elimina la mail selezionata
     */
    @FXML
    protected void onDeleteButtonClick() throws InterruptedException {

        Thread deletemail = new Thread(new ClientConnection(model, user, "deleteMail", selectedEmail));
        deletemail.start();
        /*Non aspetto l'eliminazione perché posso sempre aggiornare dopo*/
        model.deleteEmail(selectedEmail);
        updateDetailView(emptyEmail);
    }

    /**
     * Risponde solo al mittente
     */
    @FXML
    protected void onReplyButtonClick() {
        buildScene();
    }

    /**
     * Risponde al mittente e agli altri riceventi
     */
    @FXML
    protected void onReplyAtAllButtonClick() {
        buildScene();
    }

    /**
     * Inoltra la mail
     */
    @FXML
    protected void onForwardButtonClick() {
        buildScene();
    }
    @FXML
    protected void writeButtonClick() {
        buildScene();
    }

    private void buildScene(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("comp_message.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);

            ComposeController composecontroller = new ComposeController();
            fxmlLoader.setController(composecontroller);

            /*EmailModel email = new EmailModel(user);
            composecontroller.setModel(email);*/

            stage.setTitle("Write your mail");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }
}