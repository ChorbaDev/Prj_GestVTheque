package Modele;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;


public class Produit {
    protected IntegerProperty idProduit;
    protected StringProperty titreProduit;
    protected IntegerProperty stockProduit;
    protected DoubleProperty tarifProduit;
    protected StringProperty typeProduit;
    protected ImageView imageProduit;

    public Produit(int idProduit, String titreProduit, int stockProduit, double tarifProduit, String type)
    {
        this.idProduit = new SimpleIntegerProperty(idProduit);
        this.titreProduit = new SimpleStringProperty(titreProduit);
        this.stockProduit = new SimpleIntegerProperty(stockProduit);
        this.tarifProduit = new SimpleDoubleProperty(tarifProduit);
        this.typeProduit = new SimpleStringProperty(type);
    }

    public Produit(String titreProduit, int stockProduit, double tarifProduit, String typeProduit)
    {
        this.titreProduit = new SimpleStringProperty(titreProduit);
        this.tarifProduit = new SimpleDoubleProperty(tarifProduit);
        this.stockProduit = new SimpleIntegerProperty(stockProduit);
        this.typeProduit = new SimpleStringProperty(typeProduit);
    }

    public Produit(int idProduit, String titreProduit, int stockProduit, double tarifProduit, String type, ImageView imageProduit)
    {
        this.idProduit = new SimpleIntegerProperty(idProduit);
        this.titreProduit = new SimpleStringProperty(titreProduit);
        this.stockProduit = new SimpleIntegerProperty(stockProduit);
        this.tarifProduit = new SimpleDoubleProperty(tarifProduit);
        this.typeProduit = new SimpleStringProperty(type);
        this.imageProduit = imageProduit;
    }

    public ImageView getImageProduit()
    {
        return imageProduit;
    }

    public void setImageProduit(ImageView imageProduit)
    {
        this.imageProduit = imageProduit;
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

    public int getStockProduit()
    {
        return stockProduit.get();
    }

    public void setStockProduit(int stockProduit)
    {
        this.stockProduit.set(stockProduit);
    }

    public IntegerProperty stockProduitProperty()
    {
        return stockProduit;
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
