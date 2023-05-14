package com.riccardo.server.controller;

import com.riccardo.client.model.Email;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    @Override
    public void run() {
        try {
            //inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();

            /*String user = inStream.readUTF();
            controller.updateLog(user + "mails updated");*/

            List<Email> emails = readJson("Katherine Johnson");

            outStream.writeObject(emails);
            outStream.flush();

        } catch (IOException e) {
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

                if(Objects.equals(name, (String) user.get("name"))){
                    JSONArray mails = (JSONArray) user.get("mails");
                    for (int j = 0; j < mails.size(); j++){

                        JSONObject reademail = (JSONObject) mails.get(j);
                        System.out.println(reademail.get("from"));

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
                                //List.of(reademail.get("to").toString()),
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
