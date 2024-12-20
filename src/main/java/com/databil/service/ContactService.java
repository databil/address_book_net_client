package com.databil.service;

import com.databil.model.Contact;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ContactService {

    final String SERVER_IP = "127.0.0.0";
    final int SERVER_PORT = 3333;
    ObjectMapper objectMapper = new ObjectMapper();

    List<Contact> contacts = new ArrayList<>();

    public ContactService() {

    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void readContactsFromServer() {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("command:get:list");
            out.flush();
            //IO blocks waiting for list of contacts
            String response = in.readLine();
            contacts = objectMapper.readValue(response, new TypeReference<ArrayList<Contact>>() {});

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
