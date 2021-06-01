package Modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Auteur extends Personne{
    private IntegerProperty idAuteur;
    private StringProperty resume;
    private IntegerProperty nbLivres;

    public int getNbLivres() {
        return nbLivres.get();
    }

    public IntegerProperty nbLivresProperty() {
        return nbLivres;
    }

    public void setNbLivres(int nbLivres) {
        this.nbLivres.set(nbLivres);
    }

    public Auteur(int idAuteur, String nomAuteur, String prenomAuteur, String resume) {
        super(nomAuteur, prenomAuteur);
        this.idAuteur =new SimpleIntegerProperty(idAuteur);
        this.resume = new SimpleStringProperty(resume);
    }

    public Auteur(String nomAuteur, String prenomAuteur, String resume) {
        super(nomAuteur, prenomAuteur);
        this.resume =new SimpleStringProperty(resume);
    }

    public Auteur(int idAuteur, String nomAuteur, String prenomAuteur, String resume, int nbLivres) {
        super(nomAuteur, prenomAuteur);
        this.idAuteur =new SimpleIntegerProperty(idAuteur);
        this.resume = new SimpleStringProperty(resume);
        this.nbLivres = new SimpleIntegerProperty(nbLivres);
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
