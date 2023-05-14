package com.riccardo.client.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
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

    /**
     *genera email random da aggiungere alla lista di email, ese verranno mostrate nella ui
     */
    public void generateRandomEmails(int n) {
        String[] people = new String[] {"Paolo", "Alessandro", "Enrico", "Giulia", "Gaia", "Simone"};
        String[] subjects = new String[] {
                "Importante", "A proposito della nostra ultima conversazione", "Tanto va la gatta al lardo",
                "Non dimenticare...", "Domani scuola" };
        String[] texts = new String[] {
                "È necessario che ci parliamo di persona, per mail rischiamo sempre fraintendimenti",
                "Ricordati di comprare il latte tornando a casa",
                "L'appuntamento è per domani alle 9, ci vediamo al solito posto",
                "Ho sempre pensato valesse 42, tu sai di cosa parlo"
        };
        Random r = new Random();
        for (int i=0; i<n; i++) {
            Email email = new Email(
                    people[r.nextInt(people.length)],
                    List.of(people[r.nextInt(people.length)]),
                    subjects[r.nextInt(subjects.length)],
                    texts[r.nextInt(texts.length)]);
            inboxContent.add(email);
        }
    }
}

