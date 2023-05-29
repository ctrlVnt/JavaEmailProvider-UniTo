package com.riccardo.client;

import com.riccardo.client.controller.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        ArrayList<String> account = new ArrayList<>();
        account.add("katherine.johnson@unito.it");
        account.add("doodo345@mymail.com");
        account.add("hford@gmail.com");
        account.add("riccardoventurini@yahoo.it");
        account.add("francois.marshal@usmb.fr");

        Random random = new Random();
        int randomIndex = random.nextInt(account.size());
        String user = account.get(randomIndex);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("main.fxml"));

            ClientController clientcontroller = new ClientController(user);
            fxmlLoader.setController(clientcontroller);
            clientcontroller.setCloseEventHandler(stage);

            Scene scene = new Scene(fxmlLoader.load(), 900, 600);

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