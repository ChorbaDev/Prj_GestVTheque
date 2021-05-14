package Controlleur;

import Modele.Commande;
import Modele.ModelProduitsCommande;
import Modele.Produit;
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
    @FXML private TableView<ModelProduitsCommande> table;
    @FXML private TableColumn<ModelProduitsCommande,String> colType;
    @FXML private TableColumn<ModelProduitsCommande, String> colTitre;
    @FXML private TableColumn<ModelProduitsCommande, Float> colTarif;
    @FXML private TableColumn<ModelProduitsCommande, String> colDateFin;

    ObservableList<ModelProduitsCommande> obList= FXCollections.observableArrayList();
    @FXML private Label idCmd;

    @FXML private Label prixTotale;

    @FXML private Label redCmd;

    @FXML private Label dateCreationCmd;

    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    public void retour(ActionEvent e) throws IOException {
        String pathURL="/Vue/SceneCommandesClient.fxml";
        root = FXMLLoader.load(getClass().getResource(pathURL));
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private double reduction=0;
    public void getInfos(Commande cm) {
        idCmd.setText(Integer.toString(cm.getIdCommande()));
        redCmd.setText(Double.toString(cm.getReduction()));
        dateCreationCmd.setText(cm.getDateCreation());
        reduction=cm.getReduction();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colType.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande,String>("typeProduit"));
        colTitre.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande,String>("titreProduit"));
        colTarif.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande,Float>("tarifProduit"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<ModelProduitsCommande,String>("dateFinLocation"));
        table.setItems(obList);
        try {
            remplirLaListe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        remplirPrixTotale();
    }

    private void remplirPrixTotale() {
        double som=0,prixTot;
        for(int i=0;i<obList.size();i++){
            som+=obList.get(i).getTarifProduit();
        }
        prixTot=som;
        if(!redCmd.getText().isEmpty())
        prixTot*=0.9;
        System.out.println(som);
        System.out.println(prixTot);
        prixTotale.setText(Double.toString(prixTot));
    }
    public void selectProduit(){

        //dateFinLocation.setText(res.getString("dateFin"));
    }
    private void remplirLaListe() throws SQLException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql= "select produit.type ,titreProduit,tarifJounalier,dateFin from produit,concerne where produit.idProduit=concerne.idProduit and idCommande=3";
        ResultSet res=connection.createStatement().executeQuery(sql);
        while( res.next() )
        {
            obList.add(new ModelProduitsCommande(
                    res.getString("titreProduit"),
                    res.getFloat("tarifJounalier"),
                    res.getString("type"),
                    res.getString("dateFin")
                      ));
        }
    }
}
