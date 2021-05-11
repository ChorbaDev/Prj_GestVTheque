package Modele;

import javafx.scene.image.Image;

public abstract  class Produit {
    protected int    idProduit;
    protected String titreProduit;
    protected String stockProduit;
    protected double tarifProduit;
    protected Image  imageProduit;

    public Produit(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit) {
        this.idProduit = idProduit;
        this.titreProduit = titreProduit;
        this.stockProduit = stockProduit;
        this.tarifProduit = tarifProduit;
        this.imageProduit = imageProduit;
    }

    public Produit(int idProduit, String titreProduit, String stockProduit, double tarifProduit) {
        this.idProduit = idProduit;
        this.titreProduit = titreProduit;
        this.stockProduit = stockProduit;
        this.tarifProduit = tarifProduit;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getTitreProduit() {
        return titreProduit;
    }

    public void setTitreProduit(String titreProduit) {
        this.titreProduit = titreProduit;
    }

    public String getStockProduit() {
        return stockProduit;
    }

    public void setStockProduit(String stockProduit) {
        this.stockProduit = stockProduit;
    }

    public double getTarifProduit() {
        return tarifProduit;
    }

    public void setTarifProduit(double tarifProduit) {
        this.tarifProduit = tarifProduit;
    }

    public Image getImageProduit() {
        return imageProduit;
    }

    public void setImageProduit(Image imageProduit) {
        this.imageProduit = imageProduit;
    }
}
