package Modele;

public class Auteur extends Personne{
    private String resume;

    public Auteur(String nom, String prenom, String resume) {
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
