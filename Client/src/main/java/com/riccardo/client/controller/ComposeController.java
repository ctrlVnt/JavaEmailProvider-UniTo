package com.riccardo.client.controller;

import com.riccardo.client.model.Email;
import com.riccardo.client.model.EmailModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComposeController{

    @FXML
    private Label senderTextField;
    @FXML
    private TextField receivers;
    @FXML
    private TextField objectTextField;
    @FXML
    private TextArea messageEditor;
    @FXML
    private Button sendBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextFlow dangerAlert;
    private String sender;
    EmailModel email;

    /**
     * Costruttore della classe.
     * @param user   indirizzo email principale
     */
    public ComposeController(String user) {
        this.sender = user;
    }

    /**
     * Costruttore della classe.
     */
    public ComposeController() {
    }

    /**
     * Imposta il modello.
     * @param email   email selezionata
     */
    public void setModel(EmailModel email){
        this.email = email;
    }

    @FXML
    public void initialize(){
        senderTextField.textProperty().bind(email.emailAddressProperty());
        receivers.textProperty().bindBidirectional(email.receiversProperty());
        objectTextField.textProperty().bindBidirectional(email.subjectProperty());
        messageEditor.textProperty().bindBidirectional(email.textProperty());

        sendBtn.setOnMouseClicked(this::onSendButtonClick);
        cancelBtn.setOnMouseClicked(this::onCancelButtonClick);
    }

    /**
     * Annulla la scrittura di un messaggio.
     */
    private void onCancelButtonClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Invia il messaggio.
     */
    public void onSendButtonClick(MouseEvent mouseEvent) {
        String recipientsText = receivers.getText();
        String[] recipientEmails = recipientsText.split(",");

        Set<String> uniqueReceivers = new HashSet<>();
        List<String> receiversMail = new ArrayList<>();

        for (String recipient : recipientEmails) {
            String email = recipient.trim();
            if (!email.contains("@")) {
                dangerAlert.setVisible(true);
                Text errorMessage = (Text) dangerAlert.getChildren().get(0);
                errorMessage.setText("Indirizzo mail non valido o vuoto");
                return;
            }
            if (uniqueReceivers.contains(email)) {
                dangerAlert.setVisible(true);
                Text errorMessage = (Text) dangerAlert.getChildren().get(0);
                errorMessage.setText("Duplicato indirizzo mail: " + email);
                return;
            }
            uniqueReceivers.add(email);
            receiversMail.add(email);
        }

        Email email = new Email(
                senderTextField.getText(),
                receiversMail,
                objectTextField.getText(),
                messageEditor.getText());

        Thread send = new Thread(new ClientConnection(sender, "sendMail", email, (ArrayList<String>) receiversMail));
        send.start();

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

}
