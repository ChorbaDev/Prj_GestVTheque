package DAO;

import Controlleur.ConnectionClass;
import DAO.RealisateurDAO;
import Modele.Realisateur;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RealisateurDAOImpl implements RealisateurDAO {
    private static final String INSERT_REALISATEUR_SQL = "INSERT INTO realisateur (nomReal,prenomReal,resume) VALUES (?,?,?)";
    private static final String UPDATE_REALISATEUR_SQL = "update realisateur set nomReal=?, prenomReal=?, resume=? where idReal=? ";
    private static final String EXISTE_REALISATEUR_SQL = "select * from realisateur where resume=?";
    private static PreparedStatement ps;
    private static ConnectionClass connectionClass;
    private static Connection connection;

    public RealisateurDAOImpl() throws SQLException {
        connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
    }

    @Override
    public void insertRealisateur(Realisateur realisateur) throws SQLException {
        ps = connection.prepareStatement(INSERT_REALISATEUR_SQL);
        ps.setString(1, realisateur.getNom());
        ps.setString(2, realisateur.getPrenom());
        ps.setString(3, realisateur.getResume());
        ps.executeUpdate();
    }

    @Override
    public void updateRealisateur(Realisateur realisateur) throws SQLException {
        ps = connection.prepareStatement(UPDATE_REALISATEUR_SQL);
        ps.setString(1, realisateur.getNom());
        ps.setString(2, realisateur.getPrenom());
        ps.setString(3, realisateur.getResume());
        ps.setInt(4, realisateur.getIdRealisateur());
        ps.executeUpdate();
    }

    @Override
    public void remplirListeRealisateur(ObservableList<Realisateur> liste) throws SQLException {
        String sql = "SELECT idReal,nomReal,prenomReal,resume FROM realisateur";
        ResultSet res = connection.createStatement().executeQuery(sql);
        while (res.next()) {
            liste.add(new Realisateur(
                    res.getInt("idReal"),
                    res.getString("nomReal"),
                    res.getString("prenomReal"),
                    res.getString("resume")
            ));
        }
    }

    @Override
    public boolean existenceRealisateur(Realisateur realisateur) throws SQLException {
        ps = connection.prepareStatement(EXISTE_REALISATEUR_SQL);
        ps.setString(1, realisateur.getResume());
        return ps.executeQuery().next();
    }

}

