package DAO;

import Modele.Produit;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ProduitDAO {
    PreparedStatement ajouterProduit(Produit produit) throws SQLException;
    PreparedStatement modifierProduitSelectionner(Produit produit) throws SQLException;

    ResultSet modifType(int idProduit) throws SQLException;
    ResultSet modifLangue(int idProduit) throws SQLException;
    ResultSet modifAuteur(int idProduit) throws SQLException;
    ResultSet modifReal(int idProduit) throws SQLException;
    ResultSet modifAnnee(int idProduit) throws SQLException;
    ResultSet modifNbpages(int idProduit) throws SQLException;
    ResultSet modifDuree(int idProduit) throws SQLException;
    ResultSet inputProduit(int idProduit) throws SQLException;

    Image imageProduit(int idProduit) throws SQLException, IOException;
    void remplirLaListe(ObservableList<Produit> liste) throws SQLException;
    void remplirComboTypes(ObservableList<String> combo) throws SQLException;
    void remplirComboRealisateur(ObservableList<String> comboListRealisateur) throws SQLException;
    void remplirComboAuteur(ObservableList<String> comboListAuteur) throws SQLException;

    void supprimerProduit(Produit produit) throws SQLException;
}
