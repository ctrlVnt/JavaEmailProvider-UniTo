package com.riccardo.client.controller;

import com.riccardo.client.model.ClientModel;
import com.riccardo.client.model.Email;
import com.riccardo.client.model.EmailModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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

    private Stage stage;
    private String sender;
    EmailModel email;

    public ComposeController(String user) {
        this.sender = user;
    }

    public ComposeController() {
    }

    public void setModel(EmailModel email){
        this.email = email;
    }
    @FXML
    public void initialize() throws InterruptedException {

        senderTextField.textProperty().bind(email.emailAddressProperty());
        receivers.textProperty().bindBidirectional(email.receiversProperty());
        objectTextField.textProperty().bindBidirectional(email.subjectProperty());
        messageEditor.textProperty().bindBidirectional(email.textProperty());

        sendBtn.setOnMouseClicked(this::onSendButtonClick);
        cancelBtn.setOnMouseClicked(this::onCancelButtonClick);
    }

    private void onCancelButtonClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void onSendButtonClick(MouseEvent mouseEvent) {
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
        messageEditor.getText());

        Thread send = new Thread(new ClientConnection(sender, "sendMail", email, (ArrayList<String>) receiversMail));
        send.start();

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
