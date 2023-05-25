package com.riccardo.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NotifController {

    @FXML
    private Text newMailText;
    @FXML
    private Button btnOk;

    public void setNmail(int n){
        newMailText.setText("There are " + n + " new mail !");
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }
}
