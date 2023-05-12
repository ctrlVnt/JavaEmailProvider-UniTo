package com.riccardo.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;

import java.util.*;

public class ComposeController{

    @FXML
    private TextFlow dangerAlert;
    @FXML
    private TextFlow successAlert;
    @FXML
    private TextField senderTextField;
    @FXML
    private TextField recipientsTextField;
    @FXML
    private TextField objectTextField;
    @FXML
    private HTMLEditor messageEditor;


    public TextFlow getSuccessAlert() { return successAlert; }

    public TextFlow getDangerAlert() { return dangerAlert; }

    public void onCancelButtonClick(MouseEvent mouseEvent) {
    }


    /*@FXML
    public void initialize(){
        senderTextField.setEditable(false);
        senderTextField.setText(getUserEmail());
    }


    public TextField getSenderTextField() {
        return senderTextField;
    }

    public TextField getRecipientsTextField() {
        return recipientsTextField;
    }

    public TextField getObjectTextField() {
        return objectTextField;
    }

    public HTMLEditor getMessageEditor() {
        return messageEditor;
    }


    @FXML
    private void onCancelButtonClick() {
        //clearing all fields
        recipientsTextField.clear();
        objectTextField.clear();
        messageEditor.setHtmlText("");

        ClientApp.sceneController.switchTo(SceneName.MAIN);
    }


    @FXML
    private void onSendButtonClick() {
        String[] recipientsArray = recipientsTextField.getText().split("\\s*,\\s*");
        if (Arrays.stream(recipientsArray).allMatch(CommonUtil::validateEmail)){
            Email email = new Email(senderTextField.getText(),
                    new ArrayList<>(List.of(recipientsArray)),
                    objectTextField.getText(), messageEditor.getHtmlText());

            model.getClient().sendCmd(CommandName.SEND_EMAIL, email,
                    ClientApp.sceneController.getController(SceneName.COMPOSE),
                    (obj) -> Platform.runLater( () -> send(obj)), email);
        } else {
            AlertManager.showTemporizedAlert(dangerAlert, AlertText.INVALID_RECIPIENTS, 2);
        }
    }


    private void send(Object email){
        model.addEmails(Collections.singletonList((Email) email));
        //clearing all fields
        recipientsTextField.clear();
        objectTextField.clear();
        messageEditor.setHtmlText("");
        AlertManager.showSuccessSendMessage(AlertText.MESSAGE_SENT, 2);
    }*/
}
