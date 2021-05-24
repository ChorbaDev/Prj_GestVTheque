package Modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Realisateur extends Personne {
    private IntegerProperty idRealisateur;
    private StringProperty resume;

    public Realisateur(int idRealisateur, String nomRealisateur, String prenomRealisateur, String resume) {
        super(nomRealisateur, prenomRealisateur);
        this.idRealisateur =new SimpleIntegerProperty(idRealisateur);
        this.resume = new SimpleStringProperty(resume);
    }
    public Realisateur(String nomAuteur, String prenomAuteur, String resume) {
        super(nomAuteur, prenomAuteur);
        this.resume =new SimpleStringProperty(resume);
    }

    public int getIdRealisateur() {
        return idRealisateur.get();
    }

    public IntegerProperty idRealisateurProperty() {
        return idRealisateur;
    }

    public void setIdRealisateur(int idRealisateur) {
        this.idRealisateur.set(idRealisateur);
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
    public boolean equals(Realisateur rls) {
        return (rls.nom.getValue().equals(nom.getValue())) &&
                (rls.prenom.getValue().equals(prenom.getValue())) &&
                (rls.resume.getValue().equals(resume.getValue()));
    }


}
