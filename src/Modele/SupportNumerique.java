package Modele;

import javafx.scene.image.Image;

public abstract class SupportNumerique extends Produit{
        protected int duree;

        public SupportNumerique(int idProduit, String titreProduit, String stockProduit, double tarifProduit, Image imageProduit, int duree) {
                super(idProduit, titreProduit, stockProduit, tarifProduit, imageProduit);
                this.duree = duree;
        }

        public SupportNumerique(int idProduit, String titreProduit, String stockProduit, double tarifProduit, int duree) {
                super(idProduit, titreProduit, stockProduit, tarifProduit);
                this.duree = duree;
        }

        public int getDuree() {
                return duree;
        }

        public void setDuree(int duree) {
                this.duree = duree;
        }
}
