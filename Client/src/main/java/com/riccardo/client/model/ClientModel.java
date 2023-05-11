package com.riccardo.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class ClientModel {

    private final StringProperty username;

    public ClientModel() {
        this.username = new SimpleStringProperty();
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
}
