package com.riccardo.client;

import com.riccardo.client.controller.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);

            ClientController clientcontroller = new ClientController();
            fxmlLoader.setController(clientcontroller);
            clientcontroller.setCloseEventHandler(stage);

            stage.setTitle("Welcome in your mailbox!");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}