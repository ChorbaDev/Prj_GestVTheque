package Controlleur;

import Modele.Realisateur;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import java.sql.*;
import java.util.ResourceBundle;

public class EcouteurRealisateur implements Initializable {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;

    /*concerne la liste réalisateur*/
    @FXML
    private TableView<Realisateur> tblRealisateur;

    @FXML
    private TableColumn<Realisateur, Integer> colIdRealisateur;

    @FXML
    private TableColumn<Realisateur, String> colNomRealisateur;

    @FXML
    private TableColumn<Realisateur, String> colPrenomRealisateur;


    /*concerne la scene réalisateur*/
    @FXML
    private JFXTextField edtNomRealisateur;

    @FXML
    private JFXTextField edtPrenomRealisateur;

    @FXML
    private JFXTextArea mmoResumeRealisateur;
    private ObservableList<Realisateur> obList= FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colIdRealisateur.setCellValueFactory(new PropertyValueFactory<Realisateur, Integer>("idRealisateur"));
        colNomRealisateur.setCellValueFactory(new PropertyValueFactory<Realisateur, String>("nom"));
        colPrenomRealisateur.setCellValueFactory(new PropertyValueFactory<Realisateur, String>("prenom"));
        tblRealisateur.setItems(obList);
        try {
            remplirLaListe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void retour(ActionEvent e) throws IOException {
        path = "/Vue/SceneBienvenue.fxml";
        basculeScene(e, path);
    }

    public void basculeScene(ActionEvent e, String pathURL) throws IOException {
        root = FXMLLoader.load(getClass().getResource(pathURL));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void nettoyageScene() throws SQLException {
        viderChamps();
        viderListe();
        remplirLaListe();
    }

    private void remplirLaListe() throws SQLException {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        String sql = "SELECT idReal,nomReal,prenomReal,resume FROM realisateur";
        ResultSet res = connection.createStatement().executeQuery(sql);
        while (res.next()) {
            obList.add(new Realisateur(
                    res.getInt("idReal"),
                    res.getString("nomReal"),
                    res.getString("prenomReal"),
                    res.getString("resume")
            ));
        }
    }

    private void viderChamps() {
        edtNomRealisateur.setText("");
        edtPrenomRealisateur.setText("");
        mmoResumeRealisateur.setText("");
    }

    private void viderListe() {
        tblRealisateur.getItems().clear();
    }

    public void ajoutRealisateur() throws SQLException {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        String sql = "SELECT nomReal FROM realisateur WHERE resume='" + mmoResumeRealisateur.getText().replace("\'","\\\'")+"'";
        Statement statement = connection.createStatement();
        if (statement.executeQuery(sql).next()) {
            notifBuilder("Attention",
                    "le réalisateur " + edtNomRealisateur.getText() + " existe déjà dans la base de données.",
                    "/Images/warning.png");
            statement.close();
        } else if(validationDesChamps()){
            String insertReq = "INSERT INTO realisateur (nomReal,prenomReal,resume) values (?,?,?)";
            PreparedStatement statInsert = connection.prepareStatement(insertReq);
            statInsert.setString(1, edtNomRealisateur.getText());
            statInsert.setString(2, edtPrenomRealisateur.getText());
            statInsert.setString(3, mmoResumeRealisateur.getText());
            statInsert.executeUpdate();
            statInsert.close();
            notifBuilder("Opération réussie",
                    "Votre opération d'ajouter le réalisateur " + edtNomRealisateur.getText() + " est éffectué avec succès.",
                    "/Images/checked.png");
            nettoyageScene();
        }
        else{
            notifBuilder("Attention",
                    "il faut remplir tout les champs.",
                    "/Images/warning.png");
        }

    }

    private boolean validationDesChamps() {
        return !edtNomRealisateur.getText().isEmpty() && !edtPrenomRealisateur.getText().isEmpty() && !mmoResumeRealisateur.getText().isEmpty();
    }

    public void selectionRealisateur() throws SQLException, IOException {
        viderChamps();
        if (!tblRealisateur.getSelectionModel().isEmpty()) {
            Realisateur realisateurSelectionner = tblRealisateur.getSelectionModel().getSelectedItem();
            edtNomRealisateur.setText(realisateurSelectionner.getNom());
            edtPrenomRealisateur.setText(realisateurSelectionner.getPrenom());
            mmoResumeRealisateur.setText(realisateurSelectionner.getResume());
        }
    }

    public void modifierRealisateur() throws SQLException, IOException {
        if (!tblRealisateur.getSelectionModel().isEmpty()) {
            Realisateur realisateur = tblRealisateur.getSelectionModel().getSelectedItem();
            Realisateur realisateurSelectionner = new Realisateur(realisateur.getIdRealisateur(), edtNomRealisateur.getText(), edtPrenomRealisateur.getText(), mmoResumeRealisateur.getText());
            if (!realisateur.equals(realisateurSelectionner)) {
                modifierRealisateurSelectionner(realisateurSelectionner);
                notifBuilder("Opération réussie",
                        "Votre opération de modifier le réalisateur' " + realisateur.getNom() + " a réussie.",
                        "/Images/checked.png");
            }
        } else {
            notifBuilder("Attention",
                    "Il faut sélectionner un client pour pouvoir le modifier.",
                    "/Images/warning.png");
        }

    }

    private void modifierRealisateurSelectionner(Realisateur rel) throws SQLException, IOException {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        String sql = "update realisateur set nomReal=? , prenomReal=? , resume=? where idReal=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, rel.getNom());
        statement.setString(2, rel.getPrenom());
        statement.setString(3, rel.getResume());
        statement.setInt(4, rel.getIdRealisateur());
        statement.executeUpdate();
        statement.close();
        nettoyageScene();
    }


    public void notifBuilder(String titre, String texte, String pathImg) {
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
}
