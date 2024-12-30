package com.databil.ui;

import com.databil.model.Contact;

import com.databil.model.Response;
import com.databil.model.Status;
import com.databil.service.ContactService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
public class ContactForm extends GridPane {

    private final ContactService contactService;
    private TextField nameField = new TextField();
    private TextField surnameField = new TextField();
    private TextField phoneField = new TextField();

    public ContactForm(Contact contact, ContactService contactService) {

        //TO-DO implement delete

        this.contactService = contactService;
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        Label nameLabel = new Label("Name");
        Label surnameLabel = new Label("Surname");
        Label phoneLabel = new Label("Phone");

        Label statusLabel = new Label("Status: ");


        add(nameLabel, 0, 0);
        add(nameField, 1, 0);

        add(surnameLabel, 0, 1);
        add(surnameField, 1, 1);

        add(phoneLabel, 0, 2);
        add(phoneField, 1, 2);

        add(saveButton, 0, 3);
        add(cancelButton, 1, 3);

        add(statusLabel, 0, 6);

        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            contact.setName(nameField.getText());
            contact.setSurname(surnameField.getText());
            contact.setPhone(phoneField.getText());

            Response findResponse = contactService.findByPhone(contact);
            if (findResponse.status() != Status.OK) {
                statusLabel.setText(findResponse.status().toString());
                System.out.println(findResponse);
            } else {
                if (findResponse.contactList() != null) {
                    System.out.println("Updating existing contact");
                    Response updateResponse = contactService.update(contact);
                    if (updateResponse.status() == Status.OK) {
                        contactService.getContacts().add(updateResponse.contactList().getFirst());
                        statusLabel.setText("Updated");
                    } else {
                        statusLabel.setText("Error: Not Updated");
                    }
                } else {
                    System.out.println("Creating new contact");
                    Response createResponse = contactService.createContact(contact);
                    if (createResponse.status() == Status.OK) {
                        contactService.getContacts().add(createResponse.contactList().getFirst());
                        statusLabel.setText("Created");
                    } else {
                        statusLabel.setText("Error: Not Created");
                    }
                }
            }

            nameField.clear();
            surnameField.clear();

            phoneField.clear();
        });
    }

    public void setContact(Contact contact) {
        this.nameField.setText(contact.getName());
        this.surnameField.setText(contact.getSurname());
        this.phoneField.setText(contact.getPhone());
    }

}
