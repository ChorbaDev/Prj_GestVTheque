package DAO;

import Modele.Client;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ClientDAO {

    ResultSet inputClient(int idClient) throws SQLException;

    PreparedStatement modifierClientSelectionner(Client client) throws SQLException;

    Image imageClient(int idClient) throws SQLException, IOException;

    PreparedStatement insertClient(Client client) throws SQLException;

//    PreparedStatement updateClient(Client client) throws SQLException;

    void supprimerClient(Client client) throws SQLException;

    void remplirListeClient(ObservableList<Client> liste) throws SQLException;

    boolean existenceClient(Client client) throws SQLException;

    boolean trouverFedClient(int id) throws SQLException;

    String InfosClient(int idClient,String info) throws SQLException;
}
