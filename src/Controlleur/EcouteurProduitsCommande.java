package Controlleur;

import Modele.Commande;
import Modele.ModelProduitsCommande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EcouteurProduitsCommande implements Initializable {
    ObservableList<ModelProduitsCommande> obList = FXCollections.observableArrayList();
    @FXML
    private TableView<ModelProduitsCommande> table;
    @FXML
    private TableColumn<ModelProduitsCommande, String> colType;
    @FXML
    private TableColumn<ModelProduitsCommande, String> colTitre;
    @FXML
    private TableColumn<ModelProduitsCommande, Float> colTarif;
    @FXML
    private TableColumn<ModelProduitsCommande, String> colDateFin;
    @FXML
    private Label idCmd;
    @FXML
    private Label prixTotale;
    @FXML
    private Label redCmd;
    @FXML
    private Label dateCreationCmd;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private double reduction = 0;
    private double sommeDesPrix = 0;

    @FXML
    public void retour(ActionEvent e) throws IOException
    {
        String pathURL = "/Vue/SceneListeClients.fxml";
        root = FXMLLoader.load(getClass().getResource(pathURL));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void getInfos(Commande cm) throws SQLException
    {
        idCmd.setText(Integer.toString(cm.getIdCommande()));
        redCmd.setText(Double.toString(cm.getReduction()));
        dateCreationCmd.setText(cm.getDateCreation());
        reduction = cm.getReduction();
        remplirLaListe();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        colType.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande, String>("typeProduit"));
        colTitre.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande, String>("titreProduit"));
        colTarif.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande, Float>("tarifProduit"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande, String>("dateFinLocation"));
        table.setItems(obList);
    }

    /**
     * calculer le prix total de la commande actuelle et l'indiquer dans la label du prixTotale
     */
    private void remplirPrixTotale()
    {
        if (!redCmd.getText().isEmpty())
            sommeDesPrix *= (100 - Double.valueOf(redCmd.getText())) / 100.;
        prixTotale.setText(Double.toString(sommeDesPrix));
    }

    /**
     * remplir la liste des produits
     *
     * @throws SQLException
     */
    private void remplirLaListe() throws SQLException
    {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        String sql = "select produit.type ,titreProduit,tarifJounalier,dateFin from produit,concerne where produit.idProduit=concerne.idProduit and idCommande=" + idCmd.getText();
        ResultSet res = connection.createStatement().executeQuery(sql);
        while (res.next()) {
            obList.add(new ModelProduitsCommande(
                    res.getString("titreProduit"),
                    res.getFloat("tarifJounalier"),
                    res.getString("type"),
                    res.getString("dateFin")
            ));
            sommeDesPrix += res.getFloat("tarifJounalier");
        }
        remplirPrixTotale();
    }
}
