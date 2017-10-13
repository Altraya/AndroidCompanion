package androidcompanion.contact;

import java.util.ArrayList;

/**
 * Created by Jo on 15/09/2017.
 */

public class Contact {

    private String nom;
    private ArrayList<String> numbers;

    public Contact(String nom,ArrayList<String> numbers) {
        this.nom = nom;
        this.numbers = numbers;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }
}
