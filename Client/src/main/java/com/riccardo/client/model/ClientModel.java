package com.riccardo.client.model;

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

    /**
     * Costruttore della classe.
     *
     * @param emailAddress   indirizzo email
     *
     */

    public ClientModel(String emailAddress) {
        //Li passo Linked list ma va bene qualsiasi lista, serve per dirgli che stiamo lavorando con delle liste
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>();
        //Settiamo il contenuto di INBOX a alla INBOXCONTENT
        this.inbox.set(inboxContent);
        //È inutile
        this.emailAddress = new SimpleStringProperty(emailAddress);
    }

    /**
     * @return lista di email
     *
     */
    public ListProperty<Email> inboxProperty() {
        return inbox;
    }

    /**
     *
     * @return   indirizzo email della casella postale
     *
     */
    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    /**
     *
     * @return   elimina l'email specificata
     *
     */
    public void deleteEmail(Email email) {
        inboxContent.remove(email);
    }
    
    public void addInboxContent(Email email){
        inboxContent.add(email);
    }
    public int getInboxNumber(){
        return inbox.get().size();
    }

}

