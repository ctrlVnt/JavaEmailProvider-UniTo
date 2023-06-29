package com.riccardo.server.controller;

import com.riccardo.client.model.Email;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class ServerOperations implements Runnable{

    Socket socket;
    ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;
    ServerController controller;

    /**
     * Costruttore della classe.
     * @param socket   la socket da utilizzare
     * @param controller   controller della vista
     */
    public ServerOperations(Socket socket, ServerController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    /**
     * elimina il messaggio ricevuto dal client dallo storage
     */
    private synchronized void deleteConnection(){
        try {
            String usermailbox = (String)inStream.readObject();
            Email deletemail = (Email)inStream.readObject();

            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("Files/MailLists.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray users = (JSONArray) jsonObject.get("users");

            for (int i = 0; i < users.size(); i++)
            {
                JSONObject user = (JSONObject) users.get(i);
                if(Objects.equals(usermailbox, user.get("id")))
                {
                    JSONArray mails = (JSONArray) user.get("mails");
                    Iterator<JSONObject> iterator = mails.iterator();
                    while (iterator.hasNext()) {
                        JSONObject maildelete = iterator.next();
                        if(Objects.equals(deletemail.getId(), maildelete.get("id")))
                        {
                            iterator.remove();
                            controller.updateLog(usermailbox + "--> mail id : deleted");
                            updateJSON(jsonObject);
                            break;
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * aggiunge il messaggio ricevuto dal client nello storage
     */
    private synchronized void sendConnection() {
        try {
            String usermailbox = (String) inStream.readObject();
            ArrayList<String> receivers = (ArrayList<String>) inStream.readObject();
            Email email = (Email) inStream.readObject();

            HashMap<String, Boolean> recFound = new HashMap<>();
            ArrayList<String> toListNotFound = new ArrayList<>();

            JSONObject newEmail = new JSONObject();

            if(email.getSubject() == null || Objects.equals(email.getSubject(), "") || email.getSubject().trim().isEmpty()){
                newEmail.put("subjects", "< empty subject >");
            }else{
                newEmail.put("subjects", email.getSubject());
            }

            newEmail.put("from", email.getSender());
            JSONArray toList = new JSONArray();
            for (int tmp = 0; tmp < email.getReceivers().size(); tmp++){
                toList.add(email.getReceivers().get(tmp));
                recFound.put(email.getReceivers().get(tmp), false);
            }
            newEmail.put("to", toList);
            newEmail.put("text", email.getText());
            newEmail.put("date", new Date().toString());

            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("Files/MailLists.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray users = (JSONArray) jsonObject.get("users");

            long newId = 0;
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                JSONArray mails = (JSONArray) user.get("mails");

                for (int j = 0; j < mails.size(); j++) {
                    JSONObject mail = (JSONObject) mails.get(j);
                    long mailId = (Long) mail.get("id");
                    newId = Math.max(newId, mailId);
                }
            }
            newId += 1;
            newEmail.put("id", newId);

            int found = 0;
            for (int k = 0; k < receivers.size(); k++) {

                for (int i = 0; i < users.size(); i++) {
                    JSONObject user = (JSONObject) users.get(i);
                    if (receivers.get(k).equalsIgnoreCase((String) user.get("id"))) {
                        found+=1;
                        recFound.replace(receivers.get(k), true);
                        JSONArray mails = (JSONArray) user.get("mails");
                        mails.add(0, newEmail);
                    }else if(Objects.equals(receivers.get(k), "SERVER@no-reply.unito")){
                        found+=1;
                    }
                }
            }
            updateJSON(jsonObject);
            if(found < receivers.size()){
                controller.updateLog(usermailbox + "--> ERROR: mail id doesn't exist");

                JSONObject errorMail = new JSONObject();

                for (int i = 0; i < receivers.size(); i++) {
                    if (!recFound.get(receivers.get(i))) {
                        toListNotFound.add(receivers.get(i));
                    }
                }
                if(email.getSubject() == null || Objects.equals(email.getSubject(), "") || email.getSubject().trim().isEmpty()){
                    errorMail.put("subjects", "SEND ERROR: < empty subject >");
                }else{
                    errorMail.put("subjects", "SEND ERROR: " + email.getSubject());
                }
                errorMail.put("from", "SERVER@no-reply.unito");
                errorMail.put("to", email.getSender());
                errorMail.put("date", new Date().toString());
                errorMail.put("text", "SERVER MESSAGE ERROR: clients " + toListNotFound + " not found" + "\n\n" + newEmail.get("text"));
                newId +=1;
                errorMail.put("id", newId);

                for (int i = 0; i < users.size(); i++) {
                    JSONObject user = (JSONObject) users.get(i);
                    if (Objects.equals(usermailbox, user.get("id"))) {
                        JSONArray mails = (JSONArray) user.get("mails");
                        mails.add(0, errorMail);
                        break;
                    }
                }
                updateJSON(jsonObject);
            }
            controller.updateLog(usermailbox + "--> mail id : sended");

        }catch (IOException | ClassNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * verifica che non ci siano nuove mail
     */
    private void checkComunication() {
        try {
            String user = (String)inStream.readObject();

            int number = (int)inStream.readObject();

            List<Email> newEmails = readJson(user, number);

            outStream.writeObject(newEmails);
            outStream.flush();

            controller.updateLog(user + "--> mail checked");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * segnala la prima connessione di un client
     */
    private void firstConnection() {
        try {
            String user = (String)inStream.readObject();
            controller.updateLog("First connection from... " + user);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * segnala la chiusura di un client
     */
    private void lastConnection() {
        try {
            String user = (String)inStream.readObject();
            controller.updateLog(user + " closed connection");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Chiude le connessioni aperte per la comunicazione di informazioni tra client e server.
     */
    private void closeStreams() {
        try {
            if(inStream != null) {
                inStream.close();
            }

            if(outStream != null) {
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sovrascrive il contenuto già presente nel JSON file (storage).
     * @param jsonObject l'oggetto da sovrascrivere
     */
    private void updateJSON( JSONObject jsonObject ) throws IOException {
        FileWriter fileWriter = new FileWriter("Files/MailLists.json");
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Legge il contenuto del JSON file (storage).
     * @param name la mail di cui voglio ottenere le informazioni
     * @param number numero di mail ricevute dal client da confrontare con le nuove mail arrivate
     */
    private List<Email> readJson(String name, int number){
        List<Email> inboxContent = new ArrayList<>();

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("Files/MailLists.json")) {
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray users = (JSONArray) jsonObject.get("users");
            for (int i = 0; i < users.size(); i++){
                JSONObject user = (JSONObject) users.get(i);
                if(Objects.equals(name, user.get("id"))){
                    JSONArray mails = (JSONArray) user.get("mails");
                    for (int j = 0; j < mails.size()-number; j++){
                        JSONObject reademail = (JSONObject) mails.get(j);
                        Object toObj = reademail.get("to");
                        List<String> toList = null;
                        if (toObj instanceof String) {
                            toList = Collections.singletonList((String) toObj);
                        } else if (toObj instanceof JSONArray) {
                            JSONArray toArr = (JSONArray) toObj;
                            toList = new ArrayList<>(toArr.size());

                            for (Object recipient : toArr) {
                                if (recipient instanceof String) {
                                    toList.add((String) recipient);
                                }
                            }
                        }

                        Email email = new Email(
                                reademail.get("from").toString(),
                                toList,
                                reademail.get("subjects").toString(),
                                (reademail.get("text").toString() + "\n" + reademail.get("date").toString()));

                        email.setId((long)reademail.get("id"));

                        inboxContent.add(email);
                    }
                }
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return inboxContent;
    }

    @Override
    public void run() {
        try {
            inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();

            String op = (String)inStream.readObject();

            if(Objects.equals(op, "deleteConnection")){
                deleteConnection();
            }else if(Objects.equals(op, "sendConnection")){
                sendConnection();
            }else if(Objects.equals(op, "checkComunication")){
                checkComunication();
            }else if(Objects.equals(op, "firstConnection")) {
                firstConnection();
            }else if(Objects.equals(op, "lastConnection")) {
                lastConnection();
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeStreams();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
