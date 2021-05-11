package Modele;

import javafx.scene.image.Image;

public class ManuelScolaire extends Livre{
    public ManuelScolaire(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int nbPages, Auteur auteur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit, nbPages, auteur);
    }

    public ManuelScolaire(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int nbPages, Auteur auteur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, nbPages, auteur);
    }
}
