package Modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Auteur extends Personne{
    private IntegerProperty idAuteur;
    private StringProperty resume;

    public Auteur(int idAuteur, String nomAuteur, String prenomAuteur, String resume) {
        super(nomAuteur, prenomAuteur);
        this.idAuteur =new SimpleIntegerProperty(idAuteur);
        this.resume = new SimpleStringProperty(resume);
    }

    public Auteur(String nomAuteur, String prenomAuteur, String resume) {
        super(nomAuteur, prenomAuteur);
        this.resume =new SimpleStringProperty(resume);
    }

    public int getIdAuteur() {
        return idAuteur.get();
    }

    public IntegerProperty idAuteurProperty() {
        return idAuteur;
    }

    public void setIdAuteur(int idAuteur) {
        this.idAuteur.set(idAuteur);
    }

    public String getResume() {
        return resume.get();
    }

    public StringProperty resumeProperty() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume.set(resume);
    }

    public boolean equals(Auteur atr) {
        return (atr.nom.getValue().equals(nom.getValue())) &&
                (atr.prenom.getValue().equals(prenom.getValue())) &&
                (atr.resume.getValue().equals(resume.getValue()));
    }
}
