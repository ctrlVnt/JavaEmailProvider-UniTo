package com.riccardo.client;

import com.riccardo.client.controller.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Riccardo Venturini
 */

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        try {
            BorderPane root = new BorderPane();
            FXMLLoader listLoader = new FXMLLoader(getClass().getResource("list_label.fxml"));
            root.setLeft(listLoader.load());
            ClientController.ListLabelController listLabelController = new ClientController.ListLabelController();
            listLoader.setController(listLabelController);

            FXMLLoader editorLoader = new FXMLLoader(getClass().getResource("list_mail.fxml"));
            root.setCenter(editorLoader.load());
            ClientController.ListMailController listMailController = new ClientController.ListMailController();
            editorLoader.setController(listMailController);


            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("profile_info.fxml"));
            root.setTop(menuLoader.load());
            ClientController.ProfileInfoController profileInfoController = new ClientController.ProfileInfoController();
            menuLoader.setController(profileInfoController);

        /*DataModel model = new DataModel();
        listLabelController.initModel(model);
        editorController.initModel(model);
        menuController.initModel(model);*/

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Welcome in your mailbox!");
            stage.show();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}