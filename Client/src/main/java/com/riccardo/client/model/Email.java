package com.riccardo.client.model;

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
     * @return      il mittente della mail.
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return      la lista dei destinatari della mail.
     */
    public List<String> getReceivers() {
        return receivers;
    }

    /**
     * @return      l'oggetto della mail.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return      il testo della mail.
     */
    public String getText() {
        return text;
    }

    /**
     * @return      il testo della mail formattato con un tab.
     */
    public String getTextTab() {
        String[] lines = text.split("\n");
        StringBuilder tabbedText = new StringBuilder();
        for (String line : lines) {
            tabbedText.append("|").append("\t").append(line).append("\n");
        }
        return tabbedText.toString();
    }


    /**
     * @return     l'id della mail.
     */
    public long getId() {
        return id;
    }

    /**
     * @return      la data di invio della mail.
     */
    public String getDate() {
        return date;
    }

    /**
     * imposta l'id della mail
     * @param id      l'id della mail.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * imposta la data di invio della mail
     * @param date      la data di invio della mail.
     */
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.join(" - ", List.of(this.sender,this.subject));
    }
}
