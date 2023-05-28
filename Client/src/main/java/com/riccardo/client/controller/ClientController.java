package com.riccardo.client.controller;

import com.riccardo.client.Client;
import com.riccardo.client.model.ClientModel;
import com.riccardo.client.model.Email;
import com.riccardo.client.model.EmailModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.util.*;

public class ClientController {
    @FXML
    public Label allertConnection;

    @FXML
    private Label lblFrom;

    @FXML
    private Label lblTo;

    @FXML
    private Label lblSubject;

    @FXML
    private Label lblUsername;

    @FXML
    private TextArea txtEmailContent;

    @FXML
    private ListView<Email> lstEmails;

    @FXML
    private Button btnReply;

    @FXML
    private Button btnReplyatall;

    @FXML
    private Button btnForward;

    @FXML
    private Button btnDelete;

    private ClientModel model;
    private Email selectedEmail;
    private Email emptyEmail;

    private String user;

    private BooleanProperty replyButtonDisabled = new SimpleBooleanProperty(true);
    @FXML
    public void initialize() throws InterruptedException {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        //istanza nuovo client
        ArrayList<String> account = new ArrayList<>();
        account.add("katherine.johnson@unito.it");
        account.add("doodo345@mymail.com");
        account.add("hford@gmail.com");
        account.add("riccardoventurini@yahoo.it");
        account.add("francois.marshal@usmb.fr");

        Random random = new Random();
        int randomIndex = random.nextInt(account.size());
        user = account.get(randomIndex);
        //user = "Katherine.johnson@unito.it";
        model = new ClientModel(user);

        selectedEmail = null;

        //binding tra lstEmails e inboxProperty
        lstEmails.itemsProperty().bind(model.inboxProperty());
        //fa showSelectedEmail quando clicco sopra, ma non è strettamente necessario, potrei settarlo
        // nel SceneBuilder l'apertura di quel metodo e sarebbe equivalente, come onDeleteButtonClick
        lstEmails.setOnMouseClicked(this::showSelectedEmail);
        //Non troppo utile
        lblUsername.textProperty().bind(model.emailAddressProperty());
        allertConnection.textProperty().bind(model.allertConnectionProperty());

        /*Mail vuota*/
        emptyEmail = new Email("", List.of(""), "", "");

        updateDetailView(emptyEmail);

        btnReply.disableProperty().bind(replyButtonDisabled);
        btnReplyatall.disableProperty().bind(replyButtonDisabled);
        btnForward.disableProperty().bind(replyButtonDisabled);
        btnDelete.disableProperty().bind(replyButtonDisabled);

        Thread checkNewMails = new Thread(new ClientConnection(model, user, "checkNewMails"));
        checkNewMails.start();
    }

    /**
     * Mostra la mail selezionata nella vista.
     */
    protected void showSelectedEmail(MouseEvent mouseEvent) {
        Email email = lstEmails.getSelectionModel().getSelectedItem();
        selectedEmail = email;
        updateDetailView(email);
        replyButtonDisabled.set(selectedEmail == null);
    }

    /**
     * Aggiorna la vista con la mail selezionata.
     */
    protected void updateDetailView(Email email) {
        if(email != null) {
            lblFrom.setText(email.getSender());
            lblTo.setText(String.join(", ", email.getReceivers()));
            lblSubject.setText(email.getSubject());
            txtEmailContent.setText(email.getText());
        }
    }

    /**
     * Elimina la mail selezionata.
     */
    @FXML
    protected void onDeleteButtonClick(){
        Thread deletemail = new Thread(new ClientConnection(model, user, "deleteMail", selectedEmail));
        deletemail.start();
        model.deleteEmail(selectedEmail);
        updateDetailView(emptyEmail);
        replyButtonDisabled.set(true);
    }

    /**
     * Risponde solo al mittente.
     */
    @FXML
    protected void onReplyButtonClick() {
        EmailModel email = new EmailModel(user);
        email.setSubject("RE: " + selectedEmail.getSubject());
        email.setReceivers(selectedEmail.getSender());
        email.setText("\n\n-----------------------------------------------------------------------------------------------------\n" + "from: " + selectedEmail.getSender() +"\n" + "to: " + selectedEmail.getReceivers() + "\n\n" + selectedEmail.getTextTab());
        buildScene(email);
    }

    /**
     * Risponde al mittente e agli altri destinatari.
     */
    @FXML
    protected void onReplyAtAllButtonClick() {
        EmailModel email = new EmailModel(user);
        email.setSubject("RE: " + selectedEmail.getSubject());

        Set<String> recipients = new LinkedHashSet<>();
        recipients.add(selectedEmail.getSender());
        recipients.addAll(selectedEmail.getReceivers());

        StringBuilder receiversText = new StringBuilder();
        Iterator<String> iterator = recipients.iterator();
        while (iterator.hasNext()) {
            String recipient = iterator.next();
            if (!recipient.equals(user)) {
                receiversText.append(recipient);
                if (iterator.hasNext()) {
                    receiversText.append(", ");
                }
            }
        }
        if (receiversText.length() > 0 && receiversText.charAt(receiversText.length() - 1) == ',') {
            receiversText.deleteCharAt(receiversText.length() - 1);
        }
        email.setReceivers(receiversText.toString());
        email.setText("\n\n-----------------------------------------------------------------------------------------------------\n" + "from: " + selectedEmail.getSender() +"\n" + "to: " + selectedEmail.getReceivers() + "\n\n" + selectedEmail.getTextTab());
        buildScene(email);
    }

    /**
     * Inoltra la mail.
     */
    @FXML
    protected void onForwardButtonClick() {
        EmailModel email = new EmailModel(user);
        email.setSubject("FW: " + selectedEmail.getSubject());
        email.setText("\n\n----------------Message forwarded------------------\n" + "from: " + selectedEmail.getSender() +"\n" + "to: " + selectedEmail.getReceivers() + "\n\n" + selectedEmail.getText() + "\n\n" + "---------------------------------------------------");
        buildScene(email);
    }

    /**
     *  Scrive una nuova mail.
     */
    @FXML
    protected void writeButtonClick() {
        EmailModel email = new EmailModel(user);
        buildScene(email);
    }

    /**
     * Costruisce la scena per aprire la schermata per digitare il messaggio.
     * @param email la mail selezionata nella lista della inbox.
     */
    private void buildScene(EmailModel email){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("comp_message.fxml"));
            ComposeController composecontroller = new ComposeController(user);
            fxmlLoader.setController(composecontroller);
            composecontroller.setModel(email);
            Scene scene = new Scene(fxmlLoader.load(), 615, 617);

            stage.setTitle("Write your mail");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    /**
     * Handler per terminare i thread e il programma stesso.
     */
    public void handleClose() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Termina i thread in esecuzione e il programma al chiudersi della finestra.
     * @param stage lo stage da chiudere
     */
    public void setCloseEventHandler(Stage stage) {
        stage.setOnCloseRequest((WindowEvent event) -> handleClose());
    }
}
