package Modele;

import javafx.scene.image.Image;

public class Livre extends Document{
    protected Auteur auteur;

    public Livre(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int nbPages, Auteur auteur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit, nbPages);
        this.auteur = auteur;
    }

    public Livre(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int nbPages, Auteur auteur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, nbPages);
        this.auteur = auteur;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }
}
