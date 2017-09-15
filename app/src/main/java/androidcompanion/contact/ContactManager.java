package androidcompanion.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

import androidcompanion.main.MyApp;

/**
 * Created by Jo on 15/09/2017.
 */

public class ContactManager {

    private ArrayList<Contact> listeContacts = new ArrayList<>();

    public ContactManager(){

    }

    public void refreshContactList() {
        ContentResolver cr = MyApp.getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            listeContacts.clear();
            System.out.println("Contact dump : ");
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    ArrayList<String> numbers = new ArrayList<>();

                    System.out.println("    "+name + " :");

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        numbers.add(phoneNo);
                        System.out.println("    "+phoneNo);
                    }

                    Contact c = new Contact(name,numbers);

                    pCur.close();
                }
            }
        }else{
            System.out.println("Could not dump.");
        }
        if(cur!=null){
            cur.close();
        }
    }

    public ArrayList<Contact> getListeContacts() {
        return listeContacts;
    }

    public void setListeContacts(ArrayList<Contact> listeContacts) {
        this.listeContacts = listeContacts;
    }
}
