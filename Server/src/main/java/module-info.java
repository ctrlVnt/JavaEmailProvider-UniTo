module com.riccardo.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.riccardo.client;


    opens com.riccardo.server to javafx.fxml;
    exports com.riccardo.server;
    exports com.riccardo.server.controller;
    opens com.riccardo.server.controller to javafx.fxml;
}