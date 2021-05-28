package DAO;

import Controlleur.ConnectionClass;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableauDAOImpl implements TableauDAO{
    private static final String CM_PAR_CLIENT="select count(*) as n from client,commande where client.idClient=commande.idClient and commande.idClient=?";
    private static final String LISTE_TYPE_PD="select idType from typeproduit";
    private static final String PR_TYPE_SQL=
            "select count(*) as n \n" +
            "from produit,concerne\n" +
            "where produit.type=? and produit.idProduit=concerne.idProduit;";
    ProduitDAO pd=new ProduitDAOImpl();
    ClientDAOImpl cl=new ClientDAOImpl();

    private static PreparedStatement ps1;
    private static PreparedStatement ps2;
    private static ConnectionClass connectionClass;
    private static Connection connection;

    public TableauDAOImpl() throws SQLException {
        connectionClass=new ConnectionClass();
        connection=connectionClass.getConnection();
    }
    @Override
    public void remplirBar(XYChart.Series set) throws SQLException {
        ps1=connection.prepareStatement(cl.LISTE_CLIENTS_SQL);
        ResultSet res1=ps1.executeQuery();
        while(res1.next()){
            ps2=connection.prepareStatement(CM_PAR_CLIENT);
            int id=res1.getInt("idClient");
            String nom=res1.getString("nomClient");
            ps2.setInt(1,id);
            ResultSet res2=ps2.executeQuery();
            res2.next();
            set.getData().add(new XYChart.Data<String,Integer>(nom,res2.getInt("n")));
        }

    }

    @Override
    public void remplirPie(ObservableList<PieChart.Data> pieChartData) throws SQLException {
        ps1=connection.prepareStatement(LISTE_TYPE_PD);
        ResultSet res1=ps1.executeQuery();
        while (res1.next()){
            ps2=connection.prepareStatement(PR_TYPE_SQL);
            String type=res1.getString("idType");
            ps2.setString(1,type);
            ResultSet res2=ps2.executeQuery();
            res2.next();
            pieChartData.add(new PieChart.Data(type,res2.getDouble("n")));
        }
    }

}
