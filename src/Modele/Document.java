package Modele;

import javafx.scene.image.Image;

public abstract class Document extends Produit{
    protected int nbPages;

    public Document(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int nbPages) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit);
        this.nbPages = nbPages;
    }

    public Document(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int nbPages) {
        super(idProduit, titreProduit, stockProduit, tarifProduit);
        this.nbPages = nbPages;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }
}
