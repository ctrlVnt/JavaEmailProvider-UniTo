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

            List<Email> emails = readJson(user);

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

            /*parso il Json*/
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
                        JSONObject maildelete = iterator.next();
                        if((Objects.equals(deletemail.getText(), maildelete.get("text"))) &&
                            (Objects.equals(deletemail.getSubject(), maildelete.get("subjects"))) &&
                            (Objects.equals(deletemail.getSender(), maildelete.get("from"))) &&
                            checkReceivers(deletemail.getReceivers(), maildelete.get("to")))
                        {
                            iterator.remove();

                            FileWriter fileWriter = new FileWriter("Files/MailLists.json");
                            fileWriter.write(jsonObject.toJSONString());
                            fileWriter.flush();
                            fileWriter.close();

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

    private boolean checkReceivers(List<String> sender, Object to) {
        if(sender.size() == 1){
            if (Objects.equals(sender.get(0), to.toString())){
                return true;
            }
        }
        return false;
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

    private List<Email> readJson(String name){
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
                    for (int j = 0; j < mails.size(); j++){
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
