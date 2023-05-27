package com.riccardo.client;

import com.riccardo.client.controller.ClientController;
import com.riccardo.client.model.ClientModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileReader;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Riccardo Venturini
 */

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