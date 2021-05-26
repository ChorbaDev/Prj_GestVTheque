package DAO;

import Modele.Produit;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public interface CommandeDAO {
    void remplirComboClient(ObservableList<String> clients) throws SQLException;
    void remplirListeProduits(ObservableList<Produit> produits) throws SQLException, IOException;
}
