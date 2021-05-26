package DAO;

import Controlleur.ConnectionClass;
import Modele.Produit;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandeDAOImpl implements CommandeDAO{

    private static final String SELECT_CLIENTS_SQL="select idClient, nomClient from client";
    private static final String SELECT_PRODUITS_SQL="select * from produit";
    private static PreparedStatement ps;
    private static ConnectionClass connectionClass;
    private static Connection connection;
    public CommandeDAOImpl() throws SQLException {
        connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
    }
    @Override
    public void remplirComboClient(ObservableList<String> clients) throws SQLException {
        ps=connection.prepareStatement(SELECT_CLIENTS_SQL);
        ResultSet res=ps.executeQuery();
        while(res.next()){
            String ch=res.getInt("idClient")+" "+res.getString("nomClient");
            clients.add(ch);
        }
    }
    ProduitDAO produit=new ProduitDAOImpl();
    @Override
    public void remplirListeProduits(ObservableList<Produit> produits) throws SQLException, IOException {
        ps=connection.prepareStatement(SELECT_PRODUITS_SQL);
        ResultSet res=ps.executeQuery();
        while(res.next()){
            ImageView img=new ImageView();
            img.setImage(produit.imageProduit(res.getInt("idProduit")));
            img.setFitHeight(100);
            img.setFitWidth(100);
            Produit pd=new Produit(
                    res.getInt("idProduit"),
                    res.getString("titreProduit"),
                    res.getInt("stock"),
                    res.getFloat("tarifJounalier"),
                    res.getString("type"),
                    img
            );
            produits.add(pd);
        }
    }
}
