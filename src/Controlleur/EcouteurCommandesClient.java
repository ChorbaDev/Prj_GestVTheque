package Controlleur;

import Modele.Client;
import Modele.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class EcouteurCommandesClient implements Initializable {

    ObservableList<Commande> obList = FXCollections.observableArrayList();
    @FXML
    private TableView<Commande> table;
    @FXML
    private TableColumn<Commande, Integer> idCmd;
    @FXML
    private TableColumn<Commande, Double> redCmd;
    @FXML
    private TableColumn<Commande, String> dateCmd;
    @FXML
    private Label idClient;
    @FXML
    private Label nomClient;
    @FXML
    private Label prenomClient;
    @FXML
    private Label mailClient;
    @FXML
    private Label fidele;
    @FXML
    private ImageView imgClient;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;

    @FXML
    void ensembleProduits(ActionEvent e) throws IOException, SQLException
    {
        if (!table.getSelectionModel().isEmpty()) {
            path = "/Vue/SceneProduitsCommande.fxml";
            Commande cm = new Commande(
                    table.getSelectionModel().getSelectedItem().getIdCommande(),
                    table.getSelectionModel().getSelectedItem().getReduction(),
                    table.getSelectionModel().getSelectedItem().getDateCreation()
            );
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path));
            loader.load();
            EcouteurProduitsCommande scene2 = loader.getController();
            scene2.getInfos(cm);

            root = loader.getRoot();
            basculeScene(e);
        } else {
            notifBuilder("Attention",
                    "Il faut sélectionner une commande pour pouvoir afficher ses produits.",
                    "/Images/warning.png");
        }
    }

    public void notifBuilder(String titre, String texte, String pathImg)
    {
        Image img = new Image(pathImg);
        Notifications notifBuilder = Notifications.create()
                .title(titre)
                .text(texte)
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
        notifBuilder.darkStyle();
        notifBuilder.show();
    }

    @FXML
    void supprimerCommande(ActionEvent event) throws SQLException
    {
        if (!table.getSelectionModel().isEmpty()) {
            Commande cmdSupprimer = table.getSelectionModel().getSelectedItem();
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            String sql = "DELETE FROM commande where idCmd=" + cmdSupprimer.getIdCommande();
            Statement statement = connection.createStatement();
            statement.execute(sql);
            notifBuilder("Opération réussie",
                    "Votre opération de suppression de la commande numero " + idCmd.getText() + " est éffectué avec succès.",
                    "/Images/checked.png");
        } else {
            notifBuilder("Attention",
                    "Il faut sélectionner une commande pour pouvoir le supprimer.",
                    "/Images/warning.png");
        }
        viderListe();
        remplirLaListe();
    }

    private void viderListe()
    {
        table.getItems().clear();
    }

    public void getInfos(Client cl, Image img) throws SQLException
    {
        idClient.setText(Integer.toString(cl.getIdClient()));
        nomClient.setText(cl.getNom());
        prenomClient.setText(cl.getPrenom());
        mailClient.setText(cl.getMailClient());
        String txt = cl.isClientFidele() ? "Oui" : "Non";
        fidele.setText(txt);
        imgClient.setImage(img);
        remplirLaListe();
    }

    @FXML
    void retour(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneListeClients.fxml";
        root = FXMLLoader.load(getClass().getResource(path));
        basculeScene(e);
    }

    public void basculeScene(ActionEvent e) throws IOException
    {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void remplirLaListe() throws SQLException
    {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        String sql = "select idCmd,reduction,dateCreation from commande where idClient=" + idClient.getText();
        ResultSet res = connection.createStatement().executeQuery(sql);
        while (res.next()) {
            obList.add(new Commande(
                    res.getInt("idCmd"),
                    res.getDouble("reduction"),
                    res.getString("dateCreation")
            ));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        idCmd.setCellValueFactory(new PropertyValueFactory<Commande, Integer>("idCommande"));
        redCmd.setCellValueFactory(new PropertyValueFactory<Commande, Double>("reduction"));
        dateCmd.setCellValueFactory(new PropertyValueFactory<Commande, String>("dateCreation"));
        table.setItems(obList);
    }
}
