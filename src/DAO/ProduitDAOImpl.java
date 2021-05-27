package DAO;

import Controlleur.ConnectionClass;
import DAO.ProduitDAO;
import Modele.Produit;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProduitDAOImpl implements ProduitDAO {
    private static final String INSERT_PRODUIT_SQL="insert into produit (titreProduit,tarifJounalier,produit.type,stock,anneeSortie,langue,duree,nbPages,realisateur,auteur,photo) values (?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_PRODUIT_SQL="update produit set  titreProduit=? ,tarifJounalier=? ,produit.type=? ,stock=? ,anneeSortie=?,langue=?,duree=?,nbPages=?,realisateur=?,auteur=?,photo=? where idProduit=?";
    private static final String REMPLIR_LISTE_SQL ="select idProduit,tarifJounalier,produit.type,stock,titreProduit from produit";
    private static final String REMPLIR_COMBOTYPE_SQL ="select idType from typeproduit";
    private static final String REMPLIR_COMBOREAL_SQL ="select idReal,nomReal from realisateur";
    private static final String REMPLIR_COMBOAUTEUR_SQL ="select idAuteur,nomAuteur from auteur";
    private static final String SUPPRIMER_PRODUIT_SQL="delete from produit where idProduit=?";
    private static final String TYPE_PAR_ID="SELECT produit.type from produit where idProduit=?";
    private static final String LANGUE_PAR_ID="SELECT langue from produit where idProduit=?";
    private static final String AUTEUR_PAR_ID="SELECT auteur,nomAuteur from produit,auteur where produit.auteur=auteur.idAuteur and idProduit=?";
    private static final String REAL_PAR_ID="SELECT realisateur,nomReal from produit,realisateur where produit.realisateur=realisateur.idReal and idProduit=?";
    private static final String ANNEE_PAR_ID="SELECT anneeSortie from produit where idProduit=?";
    private static final String NBPAGES_PAR_ID="SELECT nbPages from produit where idProduit=?";
    private static final String DUREE_PAR_ID="SELECT duree from produit where idProduit=?";
    private static final String PHOTO_PAR_ID="SELECT photo from produit where idProduit=?";
    private static final String TYPES_PRODUITS_SQL="select idType from typeproduit";
    //
    private static PreparedStatement ps;
    private static ConnectionClass connectionClass;
    private static Connection connection;

    public ProduitDAOImpl() throws SQLException {
        connectionClass=new ConnectionClass();
        connection=connectionClass.getConnection();
    }
    @Override
    public PreparedStatement ajouterProduit(Produit pd) throws SQLException {
        ps=connection.prepareStatement(INSERT_PRODUIT_SQL);
        ps.setString(1,pd.getTitreProduit());
        ps.setDouble(2,pd.getTarifProduit());
        ps.setString(3,pd.getTypeProduit());
        ps.setInt(4,pd.getStockProduit());
        return ps;
    }

    @Override
    public  PreparedStatement modifierProduitSelectionner(Produit pd) throws SQLException {
        ps=connection.prepareStatement(UPDATE_PRODUIT_SQL);
        ps.setString(1,pd.getTitreProduit());
        ps.setDouble(2,pd.getTarifProduit());
        ps.setString(3,pd.getTypeProduit());
        ps.setInt   (4,pd.getStockProduit());
        ps.setInt   (12,pd.getIdProduit());
        return ps;
    }

    @Override
    public void supprimerProduit(Produit produit) throws SQLException {
        ps=connection.prepareStatement(SUPPRIMER_PRODUIT_SQL);
        ps.setInt(1,produit.getIdProduit());
        ps.executeUpdate();
    }


    @Override
    public ResultSet modifType(int idProduit) throws SQLException {
        ps=connection.prepareStatement(TYPE_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public ResultSet modifLangue(int idProduit) throws SQLException {
        ps=connection.prepareStatement(LANGUE_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public ResultSet modifAuteur(int idProduit) throws SQLException {
        ps=connection.prepareStatement(AUTEUR_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public ResultSet modifReal(int idProduit) throws SQLException {
        ps=connection.prepareStatement(REAL_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public ResultSet modifAnnee(int idProduit) throws SQLException {
        ps=connection.prepareStatement(ANNEE_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public ResultSet modifNbpages(int idProduit) throws SQLException {
        ps=connection.prepareStatement(NBPAGES_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public ResultSet modifDuree(int idProduit) throws SQLException {
        ps=connection.prepareStatement(DUREE_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public ResultSet inputProduit(int idProduit) throws SQLException {
        ps=connection.prepareStatement(PHOTO_PAR_ID);
        ps.setInt(1,idProduit);
        return ps.executeQuery();
    }

    @Override
    public Image imageProduit(int idProduit) throws SQLException, IOException {
        ps=connection.prepareStatement(PHOTO_PAR_ID);
        ps.setInt(1,idProduit);
        ResultSet res=ps.executeQuery();
        res.next();
        InputStream is=res.getBinaryStream("photo");
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
    public void remplirLaListe(ObservableList<Produit> liste) throws SQLException {
        ps=connection.prepareStatement(REMPLIR_LISTE_SQL);
        ResultSet res = ps.executeQuery();
        while(res.next()){
            double tarif=(double) Math.round(res.getFloat("tarifJounalier") * 100) / 100;
            liste.add(new Produit(
                    res.getInt("idProduit"),
                    res.getString("titreProduit"),
                    res.getInt("stock"),
                    tarif,
                    res.getString("type")
            ));
        }
    }

    @Override
    public void remplirComboTypes(ObservableList<String> combo) throws SQLException {
        ps=connection.prepareStatement(TYPES_PRODUITS_SQL);
        ResultSet res = ps.executeQuery();
        while(res.next()){
            combo.add(res.getString("idType"));
        }
    }

    @Override
    public void remplirComboRealisateur(ObservableList<String> comboListRealisateur) throws SQLException {
        ps=connection.prepareStatement(REMPLIR_COMBOREAL_SQL);
        ResultSet res = ps.executeQuery();
        while(res.next()){
            comboListRealisateur.add(res.getInt("idReal")+" "+res.getString("nomReal"));
        }
    }

    @Override
    public void remplirComboAuteur(ObservableList<String> comboListAuteur) throws SQLException {
        ps=connection.prepareStatement(REMPLIR_COMBOAUTEUR_SQL);
        ResultSet res = ps.executeQuery();
        while(res.next()){
            comboListAuteur.add(res.getInt("idAuteur")+" "+res.getString("nomAuteur"));
        }
    }

}
