package Modele;

import java.time.LocalDate;

public class Date {
    private int jour;
    private int mois;
    private int annee;

    public Date(int jour, int mois, int annee) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }
    public Date() {
        this.jour = LocalDate.now().getDayOfMonth();
        this.mois = LocalDate.now().getMonthValue();
        this.annee = LocalDate.now().getYear();
    }
    static int nbJours(int jour, int mois, int annee) {
        return (int) ((1461 * (annee + 4800 + (mois - 14) / 12)) / 4 + (367 * (mois - 2 - 12 * ((mois - 14) / 12))) / 12 - (3 * ((annee + 4900 + (mois - 14) / 12) / 100)) / 4 + jour - 32075);
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int difference(Date d) {
        int nbj = nbJours(this.jour, this.mois, this.annee) - nbJours(d.jour, d.mois, d.annee);
        return nbj;
    }
    public boolean superieurEgale(Date d){
        return this.difference(d)>0;
    }
}
