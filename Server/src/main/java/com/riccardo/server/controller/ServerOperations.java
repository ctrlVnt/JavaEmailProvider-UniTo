package com.riccardo.server.controller;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ServerOperations implements Runnable{

    Socket incoming;
    ObjectInputStream inStream = null;
    public ServerOperations(Socket incoming) {
        this.incoming = incoming;
    }
    @Override
    public void run() {
        try {
            inStream = new ObjectInputStream(incoming.getInputStream());
            OutputStream outStream = incoming.getOutputStream();
            outStream.flush();

            PrintWriter out = new PrintWriter(outStream, true);

            out.println("Hello! Waiting for data.");

            // echo client input
            try {
                Date date = ((Date) inStream.readObject());
                System.out.println("Echo: " + date.toString());
                out.println("Echo: " + date.toString());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                incoming.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
