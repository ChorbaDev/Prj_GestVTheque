package Modele;

import Controlleur.ConnectionClass;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuteurDAOImpl implements AuteurDAO{
    private static final String INSERT_AUTEUR_SQL="INSERT INTO auteur (nomAuteur,prenomAuteur,resume) VALUES (?,?,?)";
    private static final String UPDATE_AUTEUR_SQL="update auteur set nomAuteur=?, prenomAuteur=?, resume=? where idAuteur=? ";
    private static final String EXISTE_AUTEUR_SQL="select * from auteur where resume=?";
    private static final String REMPLIR_LISTE="SELECT idAuteur,nomAuteur,prenomAuteur,resume FROM auteur";
    private static PreparedStatement ps;
    private static ConnectionClass connectionClass;
    private static Connection connection;

    public AuteurDAOImpl() throws SQLException {
        connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
    }
    @Override
    public void insertAuteur(Auteur auteur) throws SQLException {
        ps=connection.prepareStatement(INSERT_AUTEUR_SQL);
        ps.setString(1,auteur.getNom());
        ps.setString(2,auteur.getPrenom());
        ps.setString(3,auteur.getResume());
        ps.executeUpdate();
    }

    @Override
    public void updateAuteur(Auteur auteur) throws SQLException {
        ps=connection.prepareStatement(UPDATE_AUTEUR_SQL);
        ps.setString(1,auteur.getNom());
        ps.setString(2,auteur.getPrenom());
        ps.setString(3,auteur.getResume());
        ps.setInt(4,auteur.getIdAuteur());
        ps.executeUpdate();
    }

    @Override
    public void remplirListeAuteur(ObservableList<Auteur> liste) throws SQLException {
        ps=connection.prepareStatement(REMPLIR_LISTE);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            liste.add(new Auteur(
                    res.getInt("idAuteur"),
                    res.getString("nomAuteur"),
                    res.getString("prenomAuteur"),
                    res.getString("resume")
            ));
        }
    }

    @Override
    public boolean existenceAuteur(Auteur auteur) throws SQLException {
        ps=connection.prepareStatement(EXISTE_AUTEUR_SQL);
        ps.setString(1,auteur.getResume());
        return ps.executeQuery().next();
    }

}
