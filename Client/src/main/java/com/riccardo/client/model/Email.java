package com.riccardo.client.model;

import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    private long id;

    public Email() {}

    /**
     * Costruttore della classe.
     *
     * @param sender     email del mittente
     * @param receivers  emails dei destinatari
     * @param subject    oggetto della mail
     * @param text       testo della mail
     */


    public Email(String sender, List<String> receivers, String subject, String text) {
        this.sender = sender;
        this.subject = subject;
        this.text = text;
        this.receivers = new ArrayList<>(receivers);
    }

    /**
     * Costruttore della classe.
     *
     * @param id         id email
     * @param sender     email del mittente
     * @param receivers  emails dei destinatari
     * @param subject    oggetto della mail
     * @param text       testo della mail
     * @param date       ora invio mail (secondo ricezione server)
     */
    public Email(int id, String sender, List<String> receivers, String subject, String text, String date) {
        this.sender = sender;
        this.subject = subject;
        this.text = text;
        this.receivers = new ArrayList<>(receivers);
        this.date = date;
        this.id = id;
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

    public String getTextTab() {
        String[] lines = text.split("\n");
        StringBuilder tabbedText = new StringBuilder();
        for (String line : lines) {
            tabbedText.append("\t").append(line).append("\n");
        }
        return tabbedText.toString();
    }


    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return      stringa composta dagli indirizzi e-mail del mittente più destinatari
     */
    @Override
    public String toString() {
        return String.join(" - ", List.of(this.sender,this.subject));
    }
}
