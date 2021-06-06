package Modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Realisateur extends Personne {
    private IntegerProperty idRealisateur;
    private StringProperty resume;
    private IntegerProperty nbDVD;


    public Realisateur(int idRealisateur, String nomRealisateur, String prenomRealisateur, String resume)
    {
        super(nomRealisateur, prenomRealisateur);
        this.idRealisateur = new SimpleIntegerProperty(idRealisateur);
        this.resume = new SimpleStringProperty(resume);
    }

    public Realisateur(String nomAuteur, String prenomAuteur, String resume)
    {
        super(nomAuteur, prenomAuteur);
        this.resume = new SimpleStringProperty(resume);
    }

    public Realisateur(int idRealisateur, String nomRealisateur, String prenomRealisateur, String resume, int nbDVD)
    {
        super(nomRealisateur, prenomRealisateur);
        this.idRealisateur = new SimpleIntegerProperty(idRealisateur);
        this.resume = new SimpleStringProperty(resume);
        this.nbDVD = new SimpleIntegerProperty(nbDVD);
    }

    public int getIdRealisateur()
    {
        return idRealisateur.get();
    }

    public void setIdRealisateur(int idRealisateur)
    {
        this.idRealisateur.set(idRealisateur);
    }

    public IntegerProperty idRealisateurProperty()
    {
        return idRealisateur;
    }

    public String getResume()
    {
        return resume.get();
    }

    public void setResume(String resume)
    {
        this.resume.set(resume);
    }

    public StringProperty resumeProperty()
    {
        return resume;
    }

    public int getNbDVD()
    {
        return nbDVD.get();
    }

    public void setNbDVD(int nbDVD)
    {
        this.nbDVD.set(nbDVD);
    }

    public IntegerProperty nbDVDProperty()
    {
        return nbDVD;
    }

    public boolean equals(Realisateur rls)
    {
        return (rls.nom.getValue().equals(nom.getValue())) &&
                (rls.prenom.getValue().equals(prenom.getValue())) &&
                (rls.resume.getValue().equals(resume.getValue()));
    }


}
