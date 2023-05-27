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
    public ServerOperations(Socket socket, ServerController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    private synchronized void deleteConnection(){
        try {
            String usermailbox = (String)inStream.readObject();
            Email deletemail = (Email)inStream.readObject();

            /*operazione sul JSON*/
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

    private synchronized void sendConnection() {
        try {
            String usermailbox = (String) inStream.readObject();
            ArrayList<String> receivers = (ArrayList<String>) inStream.readObject();
            Email email = (Email) inStream.readObject();

            HashMap<String, Boolean> recFound = new HashMap<>();
            ArrayList<String> toListNotFound = new ArrayList<>();

            JSONObject newEmail = new JSONObject();
            newEmail.put("subjects", email.getSubject());
            newEmail.put("from", email.getSender());
            JSONArray toList = new JSONArray();
            for (int tmp = 0; tmp < email.getReceivers().size(); tmp++){
                toList.add(email.getReceivers().get(tmp));
                recFound.put(email.getReceivers().get(tmp), false);
            }
            newEmail.put("to", toList);
            newEmail.put("text", email.getText());
            newEmail.put("date", new Date().toString());

            /*operazione sul JSON*/
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
                    if (Objects.equals(receivers.get(k), user.get("id"))) {
                        found+=1;
                        recFound.replace(receivers.get(k), true);
                        JSONArray mails = (JSONArray) user.get("mails");
                        mails.add(0, newEmail);
                    }
                }
            }

            if(found < receivers.size()){
                controller.updateLog(usermailbox + "--> ERROR: mail id doesn't exist");

                for (int i = 0; i < receivers.size(); i++) {
                    if (!recFound.get(receivers.get(i))) {
                        toListNotFound.add(receivers.get(i));
                    }
                }

                newEmail.put("text", "SERVER MESSAGE ERROR: clients " + newEmail.get("receivers") + toListNotFound + " not found" + "\n\n" + newEmail.get("text"));

                for (int i = 0; i < users.size(); i++) {
                    JSONObject user = (JSONObject) users.get(i);
                    if (Objects.equals(usermailbox, user.get("id"))) {
                        JSONArray mails = (JSONArray) user.get("mails");
                        mails.add(0, newEmail);
                        break;
                    }
                }
            }
            updateJSON(jsonObject);
            controller.updateLog(usermailbox + "--> mail id : sended");

        }catch (IOException | ClassNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
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

    private void updateJSON( JSONObject jsonObject ) throws IOException {
        FileWriter fileWriter = new FileWriter("Files/MailLists.json");
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }

    private List<Email> readJson(String name, int number){
        List<Email> inboxContent = new ArrayList<>();

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("Files/MailLists.json")) {
            // Parse il file JSON in un oggetto Java
            Object obj = parser.parse(reader);
            // Converti l'oggetto Java in un oggetto JSONObject
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
                            // Caso in cui "to" è una singola stringa
                            toList = Collections.singletonList((String) toObj);
                        } else if (toObj instanceof JSONArray) {
                            // Caso in cui "to" è un array di stringhe
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
}
