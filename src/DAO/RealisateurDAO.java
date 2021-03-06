package DAO;

import Modele.Realisateur;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface RealisateurDAO {
    void insertRealisateur(Realisateur realisateur) throws SQLException;

    void updateRealisateur(Realisateur realisateur) throws SQLException;

    void remplirListeRealisateur(ObservableList<Realisateur> liste) throws SQLException;

    boolean existenceRealisateur(Realisateur realisateur) throws SQLException;

    int compterDVDRealisateur(String resume) throws SQLException;
}
