package Modele;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface  AuteurDAO {
    void insertAuteur(Auteur auteur) throws SQLException;
    void updateAuteur(Auteur auteur) throws SQLException;
    void remplirListeAuteur(ObservableList<Auteur> liste) throws SQLException;
    boolean existenceAuteur(Auteur auteur) throws SQLException;
}
