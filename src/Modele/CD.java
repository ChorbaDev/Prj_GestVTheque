package Modele;

import javafx.scene.image.Image;

public class CD extends SupportNumerique{
    private int anneeSortie;

    public CD(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int duree, int anneeSortie) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit, duree);
        this.anneeSortie = anneeSortie;
    }

    public CD(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int duree, int anneeSortie) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, duree);
        this.anneeSortie = anneeSortie;
    }

    public int getAnneeSortie() {
        return anneeSortie;
    }

    public void setAnneeSortie(int anneeSortie) {
        this.anneeSortie = anneeSortie;
    }
}
