package Modele;

import javafx.beans.property.*;

public class ProduitPanier {
    private StringProperty titreProduit;
    private IntegerProperty quantite;
    private IntegerProperty duree;
    private DoubleProperty prix;

    public ProduitPanier(String titreProduit, int quantite, int duree, double prix) {
        this.titreProduit = new SimpleStringProperty(titreProduit);
        this.quantite =new SimpleIntegerProperty(quantite) ;
        this.duree =new SimpleIntegerProperty(duree) ;
        this.prix =new SimpleDoubleProperty(prix) ;
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

    public int getQuantite() {
        return quantite.get();
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }

    public int getDuree() {
        return duree.get();
    }

    public IntegerProperty dureeProperty() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree.set(duree);
    }

    public double getPrixTotal() {
        return prix.get();
    }

    public DoubleProperty prixTotalProperty() {
        return prix;
    }

    public void setPrixTotal(double prixTotal) {
        this.prix.set(prixTotal);
    }
}
