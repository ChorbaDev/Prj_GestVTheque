package Controlleur;

import Modele.Client;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class EcouteurListeClients implements Initializable {

    /*Concerne la liste*/
    @FXML private TableView<Client> table;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colPrenom;
    @FXML private TableColumn<Client, String> colMail;
    @FXML private TableColumn<Client, Boolean> colFidele;

    ObservableList<Client> obList= FXCollections.observableArrayList();

    /*Concerne la scene*/
    @FXML private JFXTextField nom;
    @FXML private JFXTextField prenom;
    @FXML private JFXCheckBox fidele;
    @FXML private JFXTextField mail;

    public void remplirLaListe() throws SQLException
    {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT idClient,nomClient,prenomClient,clientFidele,mailClient FROM client";
        ResultSet res=connection.createStatement().executeQuery(sql);
        while( res.next() )
        {
            obList.add(new Client(
                                  res.getInt("idClient"),
                                  res.getString("nomClient"),
                                  res.getString("prenomClient"),
                                  res.getString("mailClient"),
                                  res.getBoolean("clientFidele")
                                 ));
        }

    }
    public void supprimerClient() throws SQLException {
        Client clientSupprmier=table.getSelectionModel().getSelectedItem();
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="DELETE FROM client where idClient='"+clientSupprmier.getIdClient()+"'";
        Statement statement = connection.createStatement();
        statement.execute(sql);
        vider();
        remplirLaListe();
    }

    private void vider() {
        table.getItems().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<Client,Integer>("idClient"));
        colNom.setCellValueFactory(new PropertyValueFactory<Client,String>("nomClient"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<Client,String>("prenomClient"));
        colMail.setCellValueFactory(new PropertyValueFactory<Client,String>("mailClient"));
        colFidele.setCellValueFactory(new PropertyValueFactory<Client,Boolean>("clientFidele"));
        table.setItems(obList);
        try {
            remplirLaListe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
