package DAO;

import Controlleur.ConnectionClass;
import Modele.Client;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAOImpl implements ClientDAO {
    private static final String INSERT_CLIENT_SQL = "INSERT INTO client (nomClient,prenomClient,clientFidele,pdp,mailClient) VALUES (?,?,?,?,?)";
    private static final String UPDATE_CLIENT_SQL = "update client set nomClient=? , prenomClient=? , mailClient=? ,clientFidele=?,pdp=? where idClient=?";
    private static final String DELETE_CLIENT_SQL = "delete from client where idClient=?";
    private static final String EXISTE_CLIENT_SQL = "select * from client where mailClient=?";
    private static final String PHOTO_PAR_ID="select pdp from client where idClient=?";
    private static PreparedStatement ps;
    private static ConnectionClass connectionClass;
    private static Connection connection;

    public ClientDAOImpl() throws SQLException {
        connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
    }

    @Override
    public ResultSet inputClient(int idClient) throws SQLException {
        ps=connection.prepareStatement(PHOTO_PAR_ID);
        ps.setInt(1,idClient);
        return ps.executeQuery();
    }

    @Override
    public PreparedStatement modifierClientSelectionner(Client client) throws SQLException {
        ps=connection.prepareStatement(UPDATE_CLIENT_SQL);
        ps.setString(1,client.getNom());
        ps.setString(2,client.getPrenom());
        ps.setString(3,client.getMailClient());
        ps.setBoolean(4,client.isClientFidele());
        ps.setInt(6,client.getIdClient());
        return ps;
    }

    @Override
    public Image imageClient(int idClient) throws SQLException, IOException {
        ps=connection.prepareStatement(PHOTO_PAR_ID);
        ps.setInt(1,idClient);
        ResultSet res=ps.executeQuery();
        res.next();
        InputStream is=res.getBinaryStream("pdp");
        OutputStream os=new FileOutputStream(new File("photo.jpg"));
        byte[] content=new byte[1024];

        int size=0;
        while( (size = is.read(content))!=-1 ){
            os.write(content,0,size);
        }

        os.close();
        is.close();
        Image img=new Image("file:photo.jpg",400,300,true, true);
        return img;
    }

    @Override
    public PreparedStatement insertClient(Client client) throws SQLException {
        int fid = client.isClientFidele() ? 1 : 0;
        ps = connection.prepareStatement(INSERT_CLIENT_SQL);
        ps.setString(1, client.getNom());
        ps.setString(2, client.getPrenom());
        ps.setBoolean(3, client.isClientFidele());
        ps.setString(5, client.getMailClient());
        return ps;
    }

//    @Override
//    public PreparedStatement updateClient(Client client) throws SQLException {
//        ps = connection.prepareStatement(UPDATE_CLIENT_SQL);
//        ps.setString(1, client.getNom());
//        ps.setString(2, client.getPrenom());
//        ps.setString(3, client.getMailClient());
//        ps.setBoolean(4, client.isClientFidele());
//        ps.setInt(6, client.getIdClient());
//
//        return ps;
//
//    }

    @Override
    public void supprimerClient(Client client) throws SQLException {
        ps = connection.prepareStatement(DELETE_CLIENT_SQL);
        ps.setInt(1, client.getIdClient());
        ps.executeUpdate();
    }

    @Override
    public void remplirListeClient(ObservableList<Client> liste) throws SQLException {
        String sql = "SELECT idClient,nomClient,prenomClient,clientFidele,mailClient FROM client";
        ResultSet res = connection.createStatement().executeQuery(sql);
        while (res.next()) {
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
        ps = connection.prepareStatement(EXISTE_CLIENT_SQL);
        ps.setString(1, client.getMailClient());
        return ps.executeQuery().next();

    }
}
