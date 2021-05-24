package Modele;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ClientDAO {
    void insertClient(Client client) throws SQLException;
    void updateClient(Client client) throws SQLException;
    void supprimerClient(Client client) throws SQLException;
    void remplirListeClient(ObservableList<Client> liste) throws SQLException;
    boolean existenceClient(Client client) throws SQLException;
}
