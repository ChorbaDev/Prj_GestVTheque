package Modele;

import javafx.scene.image.Image;

public class DVD extends SupportNumerique{
    Realisateur realisateur;

    public DVD(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int duree, Realisateur realisateur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit, duree);
        this.realisateur = realisateur;
    }

    public DVD(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int duree, Realisateur realisateur) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, duree);
        this.realisateur = realisateur;
    }

    public Realisateur getRealisateur() {
        return realisateur;
    }

    public void setRealisateur(Realisateur realisateur) {
        this.realisateur = realisateur;
    }
}
