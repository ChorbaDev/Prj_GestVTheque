package Modele;

import javafx.scene.image.Image;

public class Dictionnaire extends Document{
    private String langue;

    public Dictionnaire(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int nbPages, String langue) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit, nbPages);
        this.langue = langue;
    }

    public Dictionnaire(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int nbPages, String langue) {
        super(idProduit, titreProduit, stockProduit, tarifProduit, nbPages);
        this.langue = langue;
    }
    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }
}
