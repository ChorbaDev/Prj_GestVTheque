package Modele;

public class Commande {
    private String idCommande;
    private float reduction;

    public Commande(String idCommande, float reduction) {
        this.idCommande = idCommande;
        this.reduction = reduction;
    }

    public String getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(String idCommande) {
        this.idCommande = idCommande;
    }

    public float getReduction() {
        return reduction;
    }

    public void setReduction(float reduction) {
        this.reduction = reduction;
    }


}
