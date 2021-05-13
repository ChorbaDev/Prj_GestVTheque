package Modele;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Personne {
        protected StringProperty nom;
        protected StringProperty prenom;

    public Personne(String nom, String prenom) {
        this.nom =new SimpleStringProperty(nom) ;
        this.prenom = new SimpleStringProperty(prenom);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }
}
