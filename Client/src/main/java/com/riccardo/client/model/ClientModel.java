package com.riccardo.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class ClientModel {

    private final StringProperty username;
    private final StringProperty password;
    //private final ObservableList<Email> emails;

    public ClientModel() {
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        //this.emails = FXCollections.observableArrayList();
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    /*public ObservableList<Email> getEmails() {
        return emails;
    }

    public void addEmail(Email email) {
        emails.add(email);
    }

    public void removeEmail(Email email) {
        emails.remove(email);
    }

    public void clearEmails() {
        emails.clear();
    }*/
}
