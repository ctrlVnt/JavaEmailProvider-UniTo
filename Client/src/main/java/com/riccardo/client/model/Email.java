package com.riccardo.client.model;

import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una mail
 */

public class Email implements Serializable {

    private String sender;
    private List<String> receivers;
    private String subject;
    private String text;
    private String date;
    private String id;

    public Email() {}

    /**
     * Costruttore della classe.
     *
     * //@param id         id email
     * @param sender     email del mittente
     * @param receivers  emails dei destinatari
     * @param subject    oggetto della mail
     * @param text       testo della mail
     * //@param date       ora invio mail (secondo ricezione server)
     */


    public Email(String sender, List<String> receivers, String subject, String text) {
        this.sender = sender;
        this.subject = subject;
        this.text = text;
        this.receivers = new ArrayList<>(receivers);
        this.date = null;
        this.id = null;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    /**
     * @return      stringa composta dagli indirizzi e-mail del mittente più destinatari
     */
    @Override
    public String toString() {
        return String.join(" - ", List.of(this.sender,this.subject));
    }
}
