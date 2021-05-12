package Controlleur;

import Modele.Client;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.management.Notification;
import java.io.IOException;
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
    private void nettoyageScene() throws SQLException {
        viderChamps();
        viderListe();
        remplirLaListe();
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
        nettoyageScene();
    }
    public void ajoutClient() throws SQLException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT nomClient FROM client WHERE mailClient='"+mail.getText()+"'";
        Statement statement = connection.createStatement();
        if(statement.executeQuery(sql).next()){
            notifBuilder("Attention",
                    "le mail "+mail.getText()+" déja existe dans la base de données.",
                    "/Images/warning.png");
            statement.close();
        }
        else{
            int fid=fidele.isSelected()?1:0;
            String insertReq="INSERT INTO client (nomClient,prenomClient,clientFidele,mailClient) values " +
                    "( '" + nom.getText()+"' , '"+prenom.getText()+"' , "+fid+" , '"+mail.getText()+"' )";
            Statement statInsert = connection.createStatement();
            statInsert.executeUpdate(insertReq);
            statInsert.close();
            notifBuilder("Opération réussie",
                    "Votre opération d'ajouter le client "+nom.getText()+" est éffectué avec succès.",
                    "/Images/checked.png");
            nettoyageScene();
        }

    }

    public void modifierClient() throws SQLException {
        if(!table.getSelectionModel().isEmpty()){
            Client client=table.getSelectionModel().getSelectedItem();
            Client clientSelectionner=new Client(client.getIdClient(),nom.getText(),prenom.getText(),mail.getText(),fidele.isSelected());
            if(!client.equals(clientSelectionner)) {
               modifierclientSelectionner(clientSelectionner);
               notifBuilder("Opération réussie",
                       "Votre opération de modifier le client "+client.getNomClient()+" a réussie.",
                       "/Images/checked.png");
           }
        }
        else{
            notifBuilder("Attention",
                        "Il faut sélectionner un client pour pouvoir le modifier.",
                        "/Images/warning.png");
        }

    }

    private void modifierclientSelectionner(Client cl) throws SQLException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        int fid=cl.isClientFidele()?1:0;
        String sql="UPDATE client " +
                "SET nomClient='"+cl.getNomClient()+"', " +
                "prenomClient='"+cl.getPrenomClient()+"', " +
                "mailClient='"+cl.getMailClient()+"', " +
                " clientFidele="+fid+
                " where idClient='"+cl.getIdClient()+"'";
        Statement statement = connection.createStatement();
        int i=statement.executeUpdate(sql);

        statement.execute(sql);
        statement.close();
        nettoyageScene();
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
    private void viderChamps() {
        mail.setText("");
        nom.setText("");
        prenom.setText("");
        fidele.setSelected(false);
    }
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;
    public void retour(ActionEvent e) throws IOException {
        path="/Vue/SceneBienvenue.fxml";
        basculeScene(e,path);
    }
    public void ensembleDesCommandes(ActionEvent e) throws IOException {
        path="/Vue/SceneCommandesClient.fxml";
        basculeScene(e,path);
    }
    public void basculeScene(ActionEvent e,String pathURL) throws IOException {
        root = FXMLLoader.load(getClass().getResource(pathURL));
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
