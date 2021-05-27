package DAO;

import Modele.Produit;
import Modele.ProduitPanier;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public interface CommandeDAO {
    void remplirComboClient(ObservableList<String> clients) throws SQLException;
    void remplirListeProduits(ObservableList<Produit> produits) throws SQLException, IOException;
    boolean trouverFidelite(Integer idClient) throws SQLException;
    void insertCommande(int idClient) throws SQLException;
    void insertConcerne(ObservableList<ProduitPanier> ListePP) throws SQLException;
    void modifierStock(ObservableList<ProduitPanier> ListePP) throws SQLException;
}
