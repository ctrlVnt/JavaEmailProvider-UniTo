package com.riccardo.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmailModel {

    private final StringProperty sender;
    private StringProperty receivers;
    private StringProperty subject;
    private StringProperty text;

    public EmailModel(String emailAddress) {
        this.sender = new SimpleStringProperty(emailAddress);
        this.receivers = new SimpleStringProperty("");
        this.subject = new SimpleStringProperty("");
        this.text = new SimpleStringProperty("");
    }

    /**
     *
     * @return   indirizzo email della casella postale
     *
     */
    public StringProperty emailAddressProperty() {
        return sender;
    }

    public StringProperty receiversProperty() {
        return receivers;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public void setReceivers(String receivers) {
        this.receivers.set(receivers);
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
