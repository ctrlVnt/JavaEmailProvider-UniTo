package com.riccardo.client.controller;

import com.riccardo.client.model.ClientModel;
import com.riccardo.client.model.Email;
import com.riccardo.client.model.EmailModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ComposeController{

    @FXML
    private TextFlow dangerAlert;
    @FXML
    private TextFlow successAlert;
    @FXML
    private Label senderTextField;
    @FXML
    private TextField receivers;
    @FXML
    private TextField objectTextField;
    @FXML
    private HTMLEditor messageEditor;

    private Stage stage;
    private String sender;
    EmailModel email;


    public void setModel(EmailModel model) {
        this.email = email;
    }

    public TextFlow getSuccessAlert() { return successAlert; }

    public TextFlow getDangerAlert() { return dangerAlert; }

    @FXML
    public void initialize() throws InterruptedException {
        if (this.email != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        email = new EmailModel("Katherine.johnson@unito.it");
        senderTextField.textProperty().bind(email.emailAddressProperty());
    }

    @FXML
    public void onCancelButtonClick() {
        stage.close();
    }

    @FXML
    public void onSendButtonClick() {
        String recipientsText = receivers.getText();
        String[] recipientEmails = recipientsText.split(",");

        List<String> receiversMail = new ArrayList<>();

        for (String recipient : recipientEmails) {
            String email = recipient.trim().toLowerCase();
            receiversMail.add(email);
        }

        Email email = new Email(senderTextField.getText(),
        receiversMail,
        objectTextField.getText(),
        messageEditor.getHtmlText());

        Thread send = new Thread(new ClientConnection(sender, "sendMail", email, (ArrayList<String>) receiversMail));
        send.start();
    }
}
