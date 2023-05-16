package com.riccardo.client.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmailModel {

    private final StringProperty sender;
    private ListProperty<String> receivers;
    private StringProperty subject;
    private StringProperty text;

    public EmailModel(String emailAddress) {

        this.sender = new SimpleStringProperty(emailAddress);

    }

    /**
     *
     * @return   indirizzo email della casella postale
     *
     */
    public StringProperty emailAddressProperty() {
        return sender;
    }

}
