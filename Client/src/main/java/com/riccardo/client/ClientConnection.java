package com.riccardo.client;

import java.io.*;
import java.net.*;

public class ClientConnection {
    public ClientConnection() throws IOException {
        // Crea una socket e si connette al server sulla porta 1234
        Socket socket = new Socket("localhost", 1234);

        // Crea un buffer di input/output per la comunicazione con il server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Invia un messaggio al server
        out.println("Ciao server!");

        // Legge la risposta del server
        String response = in.readLine();
        System.out.println("Server risponde: " + response);

        // Chiude la connessione
        out.close();
        in.close();
        socket.close();
    }
}

