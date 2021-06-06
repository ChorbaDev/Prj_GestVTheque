package Modele;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Personne {
    protected StringProperty nom;
    protected StringProperty prenom;

    public Personne(String nom, String prenom)
    {
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
    }

    public String getNom()
    {
        return nom.get();
    }

    public void setNom(String nom)
    {
        this.nom.set(nom);
    }

    public StringProperty nomProperty()
    {
        return nom;
    }

    public String getPrenom()
    {
        return prenom.get();
    }

    public void setPrenom(String prenom)
    {
        this.prenom.set(prenom);
    }

    public StringProperty prenomProperty()
    {
        return prenom;
    }


}
