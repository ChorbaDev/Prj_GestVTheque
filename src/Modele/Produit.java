package Modele;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public   class Produit {
    protected IntegerProperty idProduit;
    protected StringProperty titreProduit;
    protected IntegerProperty stockProduit;
    protected DoubleProperty tarifProduit;
    protected StringProperty typeProduit;

    public Produit(int idProduit, String titreProduit, int stockProduit, double tarifProduit,String type) {
        this.idProduit =new SimpleIntegerProperty(idProduit) ;
        this.titreProduit =new SimpleStringProperty(titreProduit);
        this.stockProduit =new SimpleIntegerProperty (stockProduit);
        this.tarifProduit =new SimpleDoubleProperty(tarifProduit) ;
        this.typeProduit=new SimpleStringProperty(type);
    }

    public Produit(String titreProduit, int stockProduit,double tarifProduit,String typeProduit) {
        this.titreProduit =new SimpleStringProperty(titreProduit);
        this.tarifProduit =new SimpleDoubleProperty(tarifProduit) ;
        this.stockProduit =new SimpleIntegerProperty(stockProduit);
        this.typeProduit=new SimpleStringProperty(typeProduit);
    }

    public String getTypeProduit() {
        return typeProduit.get();
    }

    public StringProperty typeProduitProperty() {
        return typeProduit;
    }

    public void setTypeProduit(String typeProduit) {
        this.typeProduit.set(typeProduit);
    }

    public int getIdProduit() {
        return idProduit.get();
    }

    public IntegerProperty idProduitProperty() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit.set(idProduit);
    }

    public String getTitreProduit() {
        return titreProduit.get();
    }

    public StringProperty titreProduitProperty() {
        return titreProduit;
    }

    public void setTitreProduit(String titreProduit) {
        this.titreProduit.set(titreProduit);
    }

    public int getStockProduit() {
        return stockProduit.get();
    }

    public IntegerProperty stockProduitProperty() {
        return stockProduit;
    }

    public void setStockProduit(int stockProduit) {
        this.stockProduit.set(stockProduit);
    }

    public double getTarifProduit() {
        return tarifProduit.get();
    }

    public DoubleProperty tarifProduitProperty() {
        return tarifProduit;
    }

    public void setTarifProduit(double tarifProduit) {
        this.tarifProduit.set(tarifProduit);
    }


}
