package DAO;

import Controlleur.ConnectionClass;
import Modele.Produit;
import Modele.ProduitPanier;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;

public class CommandeDAOImpl implements CommandeDAO{

    private static final String SELECT_CLIENTS_SQL="select idClient, nomClient from client";
    private static final String SELECT_PRODUITS_SQL="select * from produit";
    private static final String STOCK_PAR_ID   ="select stock from produit where titreProduit=?";
    private static final String INSERT_COMMANDE_SQL="insert into commande (idClient,reduction,dateCreation) values (?,?,?)";
    private static final String INSERT_CONCERNE_SQL="insert into concerne values(?,?,?,?,?)";
    private static final String ID_PAR_TITRE_PD="SELECT idProduit from produit where titreProduit=?";
    private static final String SCOOP_ID="SELECT MAX(idCmd) as id from commande";
    private static final String MODIF_STOCK_SQL="update produit set stock=? where idProduit=?";
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
            double tarif=(double) Math.round(res.getFloat("tarifJounalier") * 100) / 100;
            Produit pd=new Produit(
                    res.getInt("idProduit"),
                    res.getString("titreProduit"),
                    res.getInt("stock"),
                    tarif,
                    res.getString("type"),
                    img
            );
            produits.add(pd);
        }
    }

    @Override
    public boolean trouverFidelite(Integer idClient) throws SQLException {
        ClientDAO dao=new ClientDAOImpl();
        return dao.trouverFedClient(idClient);
    }

    @Override
    public void insertCommande(int idClient) throws SQLException {
        ps=connection.prepareStatement(INSERT_COMMANDE_SQL);
        ps.setInt(1,idClient);
        int red=trouverFidelite(idClient)?10:0;
        ps.setInt(2,red);
        ps.setString(3,LocalDate.now().toString());
        ps.executeUpdate();
    }

    @Override
    public void insertConcerne(ObservableList<ProduitPanier> listePP) throws SQLException {
        ps=connection.prepareStatement(SCOOP_ID);
        ResultSet res=ps.executeQuery();
        res.next();
        int id=res.getInt("id");
        for(int i=0;i<listePP.size();i++){
            ps=connection.prepareStatement(INSERT_CONCERNE_SQL);
            ps.setInt(1,getProduit(listePP.get(i).getTitreProduit()));
            ps.setInt(2,id);
            ps.setInt(3,listePP.get(i).getQuantite());
            ps.setString(4,LocalDate.now().toString());
            Calendar cal=Calendar.getInstance();
            cal.add(Calendar.DATE,listePP.get(i).getDuree());
            LocalDate d= LocalDate.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
            ps.setString(5,d.toString());
            ps.executeUpdate();
        }
    }

    private int getProduit(String titreProduit) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(ID_PAR_TITRE_PD);
        pst.setString(1,titreProduit);
        ResultSet res=pst.executeQuery();
        res.next();
        return res.getInt("idProduit");
    }

    @Override
    public void modifierStock(ObservableList<ProduitPanier> listePP) throws SQLException {
        for(int i=0;i<listePP.size();i++){
            int qt=listePP.get(i).getQuantite();
            String titre=listePP.get(i).getTitreProduit();
            ps=connection.prepareStatement(MODIF_STOCK_SQL);
            ps.setInt(1,(getStock(titre)-qt));
            ps.setInt(2,getProduit(titre));
            ps.executeUpdate();
        }
    }

    private int getStock(String titreProduit) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(STOCK_PAR_ID);
        pst.setString(1,titreProduit);
        ResultSet res=pst.executeQuery();
        res.next();

        return res.getInt("stock");
    }
}
