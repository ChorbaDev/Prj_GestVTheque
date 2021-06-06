package Modele;

import javafx.beans.property.*;

public class ModelProduitsCommande {
    protected IntegerProperty idProduit;
    protected StringProperty titreProduit;
    protected DoubleProperty tarifProduit;
    protected StringProperty typeProduit;
    protected StringProperty dateFinLocation;

    public ModelProduitsCommande(int idProduit, String titreProduit, String dateFinLocation, double tarifProduit, String type)
    {
        this.idProduit = new SimpleIntegerProperty(idProduit);
        this.titreProduit = new SimpleStringProperty(titreProduit);
        this.dateFinLocation = new SimpleStringProperty(dateFinLocation);
        this.tarifProduit = new SimpleDoubleProperty(tarifProduit);
        this.typeProduit = new SimpleStringProperty(type);
    }

    public ModelProduitsCommande(String titreProduit, double tarifProduit, String typeProduit, String dateFinLocation)
    {
        this.titreProduit = new SimpleStringProperty(titreProduit);
        this.tarifProduit = new SimpleDoubleProperty(tarifProduit);
        this.typeProduit = new SimpleStringProperty(typeProduit);
        this.dateFinLocation = new SimpleStringProperty(dateFinLocation);
    }

    public String getDateFinLocation()
    {
        return dateFinLocation.get();
    }

    public void setDateFinLocation(String dateFinLocation)
    {
        this.dateFinLocation.set(dateFinLocation);
    }

    public StringProperty dateFinLocationProperty()
    {
        return dateFinLocation;
    }

    public String getTypeProduit()
    {
        return typeProduit.get();
    }

    public void setTypeProduit(String typeProduit)
    {
        this.typeProduit.set(typeProduit);
    }

    public StringProperty typeProduitProperty()
    {
        return typeProduit;
    }

    public int getIdProduit()
    {
        return idProduit.get();
    }

    public void setIdProduit(int idProduit)
    {
        this.idProduit.set(idProduit);
    }

    public IntegerProperty idProduitProperty()
    {
        return idProduit;
    }

    public String getTitreProduit()
    {
        return titreProduit.get();
    }

    public void setTitreProduit(String titreProduit)
    {
        this.titreProduit.set(titreProduit);
    }

    public StringProperty titreProduitProperty()
    {
        return titreProduit;
    }

    public double getTarifProduit()
    {
        return tarifProduit.get();
    }

    public void setTarifProduit(double tarifProduit)
    {
        this.tarifProduit.set(tarifProduit);
    }

    public DoubleProperty tarifProduitProperty()
    {
        return tarifProduit;
    }
}
