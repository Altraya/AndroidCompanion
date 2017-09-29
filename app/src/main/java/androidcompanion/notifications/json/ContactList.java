package androidcompanion.notifications.json;

import java.util.ArrayList;

import androidcompanion.contact.Contact;

/**
 * Created by Jo on 15/09/2017.
 */

public class ContactList extends JsonObject{

    private ArrayList<Contact> contacts;

    public ContactList(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
