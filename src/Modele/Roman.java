package Modele;

import javafx.scene.image.Image;

public class Roman extends Livre{
    public Roman(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int nbPages, Auteur auteur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit, nbPages, auteur);
    }

    public Roman(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int nbPages, Auteur auteur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, nbPages, auteur);
    }
}
