package Controlleur;

import Modele.Client;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.management.Notification;
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
    private void viderListe() {
        table.getItems().clear();
    }
    public void supprimerClient() throws SQLException {
        Client clientSupprmier=table.getSelectionModel().getSelectedItem();
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="DELETE FROM client where idClient='"+clientSupprmier.getIdClient()+"'";
        Statement statement = connection.createStatement();
        statement.execute(sql);
        viderListe();
        remplirLaListe();
    }
    public void ajoutClient(){

    }
    public void notifBuilder(String titre,String texte,String pathImg){
        Image img=new Image(pathImg);
        Notifications notifBuilder=Notifications.create()
                .title(titre)
                .text(texte)
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
        notifBuilder.darkStyle();
        notifBuilder.show();
    }
    public void modifierClient(){
        if(!table.getSelectionModel().isEmpty()){
            Client clientSelectionner=table.getSelectionModel().getSelectedItem();
        }
        else{
            notifBuilder("Attention",
                        "Il faut sélectionner un client pour pouvoir le modifier.",
                        "/Images/warning.png");
        }

    }
    public void selectionClient(){
        viderChamps();
        if(!table.getSelectionModel().isEmpty()){
            Client clientSelectionner=table.getSelectionModel().getSelectedItem();
            nom.setText(clientSelectionner.getNomClient());
            prenom.setText(clientSelectionner.getPrenomClient());
            mail.setText(clientSelectionner.getMailClient());
            if(clientSelectionner.isClientFidele()){
                fidele.setSelected(true);
            }
        }
    }

    private void viderChamps() {
        mail.setText("");
        nom.setText("");
        prenom.setText("");
        fidele.setSelected(false);
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
