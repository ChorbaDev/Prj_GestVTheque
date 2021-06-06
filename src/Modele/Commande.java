package Modele;

import javafx.beans.property.*;

public class Commande {
    private IntegerProperty idCommande;
    private DoubleProperty reduction;
    private StringProperty dateCreation;

    public Commande(int idCommande, Double reduction, String dateCreation)
    {
        this.idCommande = new SimpleIntegerProperty(idCommande);
        this.reduction = new SimpleDoubleProperty(reduction);
        this.dateCreation = new SimpleStringProperty(dateCreation);
    }

    public int getIdCommande()
    {
        return idCommande.get();
    }

    public void setIdCommande(int idCommande)
    {
        this.idCommande.set(idCommande);
    }

    public IntegerProperty idCommandeProperty()
    {
        return idCommande;
    }

    public double getReduction()
    {
        return reduction.get();
    }

    public void setReduction(double reduction)
    {
        this.reduction.set(reduction);
    }

    public DoubleProperty reductionProperty()
    {
        return reduction;
    }

    public String getDateCreation()
    {
        return dateCreation.get();
    }

    public void setDateCreation(String dateCreation)
    {
        this.dateCreation.set(dateCreation);
    }

    public StringProperty dateCreationProperty()
    {
        return dateCreation;
    }

}
