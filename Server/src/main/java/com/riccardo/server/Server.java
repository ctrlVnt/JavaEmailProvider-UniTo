package com.riccardo.server;

import com.riccardo.server.controller.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Server extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 765, 512);

            ServerController servercontroller = new ServerController();
            fxmlLoader.setController(servercontroller);
            servercontroller.setCloseEventHandler(stage);

            stage.setTitle("Server");
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