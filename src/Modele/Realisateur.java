package Modele;

public class Realisateur extends Personne {
    private String resume;

    public Realisateur(String nom, String prenom, String resume) {
        super(nom, prenom);
        this.resume = resume;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
