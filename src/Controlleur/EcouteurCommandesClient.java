package Controlleur;

import Modele.Client;
import Modele.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EcouteurCommandesClient implements Initializable {

    @FXML private TableView<Commande> table;
    @FXML private TableColumn<Commande, Integer> idCmd;
    @FXML private TableColumn<Commande, Double> redCmd;
    @FXML private TableColumn<Commande, String> dateCmd;
    ObservableList<Commande> obList= FXCollections.observableArrayList();

    @FXML private Label idClient;
    @FXML private Label nomClient;
    @FXML private Label prenomClient;
    @FXML private Label mailClient;
    @FXML private Label fidele;
    @FXML private ImageView imgClient;

    @FXML
    void ensembleProduits(ActionEvent event) {

    }

    @FXML
    void supprimerCommande(ActionEvent event) {

    }
    public void getInfos(Client cl, Image img){
        idClient.setText(Integer.toString(cl.getIdClient()));
        nomClient.setText(cl.getNom());
        prenomClient.setText(cl.getPrenom());
        mailClient.setText(cl.getMailClient());
        String txt=cl.isClientFidele()?"Oui":"Non";
        fidele.setText(txt);
        imgClient.setImage(img);
    }
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;
    @FXML
    void retour(ActionEvent e) throws IOException {
        path="/Vue/SceneListeClients.fxml";
        root = FXMLLoader.load(getClass().getResource(path));
        basculeScene(e);
    }
    public void basculeScene(ActionEvent e) throws IOException {
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void remplirLaListe() throws SQLException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="" +
                "select idCmd,reduction,dateCreation from commande where idClient="+idClient.getText();
        ResultSet res=connection.createStatement().executeQuery(sql);
        while( res.next() )
        {
            obList.add(new Commande(
                    res.getInt("idCmd"),
                    res.getDouble("reduction")*10,
                    res.getString("dateCreation")
            ));
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCmd.setCellValueFactory(new PropertyValueFactory<Commande,Integer>("idCommande"));
        redCmd.setCellValueFactory(new PropertyValueFactory<Commande,Double>("reduction"));
        dateCmd.setCellValueFactory(new PropertyValueFactory<Commande,String>("dateCreation"));
        table.setItems(obList);
        try {
            remplirLaListe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
