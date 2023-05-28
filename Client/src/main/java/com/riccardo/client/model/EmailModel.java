package com.riccardo.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmailModel {

    private final StringProperty sender;
    private StringProperty receivers;
    private StringProperty subject;
    private StringProperty text;

    /**
     * Costruttore della classe.
     * @param emailAddress   indirizzo email
     */
    public EmailModel(String emailAddress) {
        this.sender = new SimpleStringProperty(emailAddress);
        this.receivers = new SimpleStringProperty("");
        this.subject = new SimpleStringProperty("");
        this.text = new SimpleStringProperty("");
    }

    /**
     * @return   indirizzo email della casella postale
     */
    public StringProperty emailAddressProperty() {
        return sender;
    }

    /**
     * @return   i destinatari della mail.
     */
    public StringProperty receiversProperty() {
        return receivers;
    }

    /**
     * @return   l'oggetto della mail.
     */
    public StringProperty subjectProperty() {
        return subject;
    }

    /**
     * @return   il testo della mail.
     */
    public StringProperty textProperty() {
        return text;
    }

    /**
     * imposta l'oggetto della mail
     * @param subject oggetto della mail
     */
    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    /**
     * imposta i destinatari della mail
     * @param receivers destinatari della mail
     */
    public void setReceivers(String receivers) {
        this.receivers.set(receivers);
    }

    /**
     * imposta il testo della mail
     * @param text testo della mail
     */
    public void setText(String text) {
        this.text.set(text);
    }
}
