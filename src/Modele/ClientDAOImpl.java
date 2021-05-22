package Modele;

import Controlleur.ConnectionClass;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAOImpl implements ClientDAO{
    private static final String INSERT_CLIENT_SQL="INSERT INTO client (nomClient,prenomClient,clientFidele,pdp,mailClient) VALUES (?,?,?,?,?)";
    private static final String UPDATE_CLIENT_SQL="update client set nomClient=? , prenomClient=? , mailClient=? ,clientFidele=?,pdp=? where idClient=?";
    private static final String EXISTE_CLIENT_SQL="select * from client where mailClient=?";
    private static PreparedStatement ps;
    private static ConnectionClass connectionClass;
    private static Connection connection;

    public ClientDAOImpl() throws SQLException {
        connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
    }
    @Override
    public void insertClient(Client client) throws SQLException {
        ps=connection.prepareStatement(INSERT_CLIENT_SQL);
        ps.setString(1,client.getNom());
        ps.setString(2,client.getPrenom());
        ps.setBoolean(3,client.clientFideleProperty().get());
        ps.setString(4,client.);
        ps.setString(5,client.getMailClient());
        ps.executeUpdate();
        
    }

    @Override
    public void updateClient(Client client) throws SQLException {
        ps=connection.prepareStatement(UPDATE_CLIENT_SQL);
        ps.setString(1,client.getNom());
        ps.setString(2,client.getPrenom());
        ps.setString(3,client.getMailClient());
        ps.setInt(4,client.getIdClient());
        ps.executeUpdate();

    }

    @Override
    public void remplirListeClient(ObservableList<Client> liste) throws SQLException {
        String sql="SELECT idClient,nomClient,prenomClient,clientFidele,mailClient FROM client";
        ResultSet res=connection.createStatement().executeQuery(sql);
        while( res.next() )
        {
            liste.add(new Client(
                    res.getInt("idClient"),
                    res.getString("nomClient"),
                    res.getString("prenomClient"),
                    res.getString("mailClient"),
                    res.getBoolean("clientFidele")
            ));
        }

    }

    @Override
    public boolean existenceClient(Client client) throws SQLException {
        ps=connection.prepareStatement(EXISTE_CLIENT_SQL);
        ps.setString(1,client.getMailClient());
        return ps.executeQuery().next();

    }
}
