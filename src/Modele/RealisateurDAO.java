package Modele;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface RealisateurDAO {
    void insertRealisateur(Realisateur realisateur) throws SQLException;
    void updateRealisateur(Realisateur realisateur) throws SQLException;
    void remplirListeRealisateur(ObservableList<Realisateur> liste) throws SQLException;
    boolean existenceRealisateur(Realisateur realisateur) throws SQLException;
}
