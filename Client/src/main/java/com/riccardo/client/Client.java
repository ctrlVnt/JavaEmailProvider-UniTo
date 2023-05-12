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

/**
 *
 * @author Riccardo Venturini
 */

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        JSONParser parser = new JSONParser();

        String mail;

        try (FileReader reader = new FileReader("Files/Users.json")) {

            // Parse il file JSON in un oggetto Java
            Object obj = parser.parse(reader);

            // Converti l'oggetto Java in un oggetto JSONObject
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray users = (JSONArray) jsonObject.get("users");

            /*for (int i = 0; i < users.size(); i++){

                JSONObject user = (JSONObject) users.get(i);

                String name = (String) user.get("name");
                String mail = (String) user.get("mail");

                System.out.println("name: " + name);
                System.out.println("mail: " + mail);
                System.out.println();
            }*/
            JSONObject user = (JSONObject) users.get(1);
            String name = (String) user.get("name");
            mail = (String) user.get("mail");

            System.out.println("name: " + name);
            System.out.println("mail: " + mail);

        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        /*PARTE SOPRA FORSE DA SPOSTARE*/
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            ClientController clientcontroller = new ClientController();
            fxmlLoader.setController(clientcontroller);

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