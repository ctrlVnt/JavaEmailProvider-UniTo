package com.riccardo.client.model;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * Classe Client, conterrà la lista di mail che sarà il model
 */

public class ClientModel {
    private final ListProperty<Email> inbox;
    private final ObservableList<Email> inboxContent;
    private final StringProperty emailAddress;
    private final StringProperty allertConnection;
    /**
     * Costruttore della classe.
     * @param emailAddress   indirizzo email
     */

    public ClientModel(String emailAddress) {
        //Li passo Linked list ma va bene qualsiasi lista, serve per dirgli che stiamo lavorando con delle liste
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>();
        //Settiamo il contenuto di INBOX a alla INBOXCONTENT
        this.inbox.set(inboxContent);
        //È inutile
        this.emailAddress = new SimpleStringProperty(emailAddress);
        this.allertConnection =  new SimpleStringProperty();
    }

    /**
     * @return lista di email.
     */
    public ListProperty<Email> inboxProperty() {
        return inbox;
    }

    /**
     * @return   indirizzo email della casella postale.
     */
    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    /**
     * elimina l'email specificata.
     * @param email   indirizzo email da rimuovere alla lista di mail.
     */
    public void deleteEmail(Email email) {
        inboxContent.remove(email);
    }

    /**
     * aggiunge l'email specificata nella lista mail.
     * @param email   indirizzo email da aggiungere alla lista di mail.
     */
    public void addInboxContent(Email email){
        inboxContent.add(0, email);
    }
    public int getInboxNumber(){
        return inbox.get().size();
    }

    /**
     * @return allertConnection, la proprietà per l'allerta di connessione
     */
    public StringProperty allertConnectionProperty() {
        return allertConnection;
    }

    /**
     * Imposta il messaggio di allerta di connessione
     * @param message il messaggio di allerta
     */
    public void setAllertConnection(String message) {
        Platform.runLater(() -> allertConnection.set(message));
    }
}

