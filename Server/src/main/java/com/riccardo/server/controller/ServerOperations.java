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

    private void firstConnection(){
        try {
            String user = (String)inStream.readObject();
            controller.updateLog(user + "mails updated");

            List<Email> emails = readJson(user, 0);

            outStream.writeObject(emails);
            outStream.flush();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteConnection(){
        try {
            String usermailbox = (String)inStream.readObject();
            Email deletemail = (Email)inStream.readObject();
            controller.updateLog(usermailbox + " ask to delete mail");

            /*operazione sul JSON*/
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("Files/MailLists.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray users = (JSONArray) jsonObject.get("users");

            for (int i = 0; i < users.size(); i++)
            {
                JSONObject user = (JSONObject) users.get(i);
                if(Objects.equals(usermailbox, user.get("mail")))
                {
                    JSONArray mails = (JSONArray) user.get("mails");
                    Iterator<JSONObject> iterator = mails.iterator();
                    while (iterator.hasNext()) {
                        System.out.println("intrappola");
                        JSONObject maildelete = iterator.next();
                        if((Objects.equals(deletemail.getText(), maildelete.get("text"))) &&
                            (Objects.equals(deletemail.getSubject(), maildelete.get("subjects"))) &&
                            (Objects.equals(deletemail.getSender(), maildelete.get("from"))) &&
                            checkReceivers(deletemail.getReceivers(), maildelete.get("to")))
                        {
                            iterator.remove();

                            updateJSON(jsonObject);

                            System.out.println("mail deleted");
                            break;
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkReceivers(List<String> receivers, Object to) {
        if(receivers.size() == 1){
            if (Objects.equals(receivers.get(0), to.toString())){
                return true;
            }
        }else{
            if (to instanceof List<?>) {
                List<?> toList = (List<?>) to;
                if (toList.size() == receivers.size()) {
                    boolean allMatch = true;
                    for (int i = 0; i < toList.size(); i++) {
                        if (!Objects.equals(receivers.get(i), toList.get(i).toString())) {
                            allMatch = false;
                            break;
                        }
                    }
                    if (allMatch) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void sendConnection() {
        try {
            String usermailbox = (String) inStream.readObject();
            ArrayList<String> receivers = (ArrayList<String>) inStream.readObject();
            Email email = (Email) inStream.readObject();

            JSONObject newEmail = new JSONObject();
            newEmail.put("subjects", email.getSubject());
            newEmail.put("from", email.getSender());
            JSONArray toList = new JSONArray();
            for (int tmp = 0; tmp < email.getReceivers().size(); tmp++){
                if(tmp < email.getReceivers().size() - 1){
                    toList.add(email.getReceivers().get(tmp) + ",");
                }else{
                    toList.add(email.getReceivers().get(tmp));
                }

            }
            newEmail.put("to", toList);
            newEmail.put("text", email.getText());

            controller.updateLog(usermailbox + " send mail");

            /*operazione sul JSON*/
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("Files/MailLists.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray users = (JSONArray) jsonObject.get("users");

            for (int k = 0; k < receivers.size(); k++) {

                boolean found = false;

                for (int i = 0; i < users.size() && !found; i++) {
                    JSONObject user = (JSONObject) users.get(i);
                    if (Objects.equals(receivers.get(k), user.get("mail"))) {
                        found = true;
                        JSONArray mails = (JSONArray) user.get("mails");
                        mails.add(0, newEmail);
                    }
                }

                if(!found){
                    System.out.println("mail address doesn't found");
                }
            }

            updateJSON(jsonObject);
            System.out.println("emails sended");

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

            if(Objects.equals(op, "firstConnection")){
                firstConnection();
            }else if(Objects.equals(op, "deleteConnection")){
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
                if(Objects.equals(name, user.get("mail"))){
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
                                reademail.get("text").toString());
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
