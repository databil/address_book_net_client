package com.databil.service;

import com.databil.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

        Command command = new Command(CommandEnum.LIST_COMMAND, null);
        Response response = sendCommand(command);
        contacts = response.contactList();
    }

    public Response createContact(Contact contact) {

        Command command = new Command(CommandEnum.NEW_COMMAND, contact);

        Response response = sendCommand(command);
        contacts = response.contactList();
        return response;
    }

    public Response sendCommand(Command command) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String commandAsJsonString = objectMapper.writeValueAsString(command);

            System.out.println("sending command to server " + commandAsJsonString);
            out.println(commandAsJsonString);
            //IO blocks waiting for list of contacts
            String responseString = in.readLine();

            return objectMapper.readValue(responseString, Response.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response findByPhone(Contact contact) {
        Command command = new Command(CommandEnum.FIND_COMMAND, contact);
        return sendCommand(command);
    }

    public Response update(Contact newContact) {
        Command command = new Command(CommandEnum.UPDATE_COMMAND, newContact);
        return sendCommand(command);
    }
}
