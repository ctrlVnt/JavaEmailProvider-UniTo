module com.riccardo.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires javafx.web;


    opens com.riccardo.client to javafx.fxml;
    exports com.riccardo.client;
    exports com.riccardo.client.controller;
    opens com.riccardo.client.controller to javafx.fxml;
    exports com.riccardo.client.model;
}