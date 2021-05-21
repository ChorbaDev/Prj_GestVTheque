package Controlleur;

import Modele.Auteur;
import Modele.AuteurDAOImpl;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.util.*;
import java.util.ResourceBundle;
import org.controlsfx.control.Notifications;
import java.io.IOException;
import java.net.URL;
import java.sql.*;


public class EcouteurAuteur implements Initializable {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;
    private AuteurDAOImpl auteurDao;
    /*en rapport avec la liste d'auteur*/
    @FXML private TableView<Auteur> tblAuteur;
    @FXML private TableColumn<Auteur, Integer> colIdAuteur;
    @FXML private TableColumn<Auteur, String> colNomAuteur;
    @FXML private TableColumn<Auteur, String> colPrenomAuteur;

    /*champs en relation avec l'auteur*/
    @FXML private JFXTextField edtNomAuteur;
    @FXML private JFXTextField edtPrenomAuteur;
    @FXML private JFXTextArea mmoResumeAuteur;

    private final ObservableList<Auteur> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colIdAuteur.setCellValueFactory(new PropertyValueFactory<Auteur, Integer>("idAuteur"));
        colNomAuteur.setCellValueFactory(new PropertyValueFactory<Auteur, String>("nom"));
        colPrenomAuteur.setCellValueFactory(new PropertyValueFactory<Auteur, String>("prenom"));
        tblAuteur.setItems(obList);
        try {
            auteurDao=new AuteurDAOImpl();
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
        auteurDao.remplirListeAuteur(obList);
    }

    private void viderChamps() {
        edtNomAuteur.setText("");
        edtPrenomAuteur.setText("");
        mmoResumeAuteur.setText("");
    }

    private void viderListe() {
        tblAuteur.getItems().clear();
    }

    public void ajoutAuteur() throws SQLException {
        Auteur auteur=new Auteur(edtNomAuteur.getText().trim(),edtPrenomAuteur.getText().trim(),mmoResumeAuteur.getText().trim());
        if (auteurDao.existenceAuteur(auteur)) {
            notifBuilder("Attention",
                    "l'auteur " + edtNomAuteur.getText() + " existe déjà dans la base de données.",
                    "/Images/warning.png");
        } else if(validationDesChamps()){
            auteurDao.insertAuteur(auteur);
            notifBuilder("Opération réussie",
                    "Votre opération d'ajouter l'auteur " + edtNomAuteur.getText() + " est éffectué avec succès.",
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
        return !edtNomAuteur.getText().isEmpty() && !edtPrenomAuteur.getText().isEmpty() && !mmoResumeAuteur.getText().isEmpty();
    }

    public void selectionAuteur() throws SQLException, IOException {
        viderChamps();
        if (!tblAuteur.getSelectionModel().isEmpty()) {
            Auteur auteurSelectionner = tblAuteur.getSelectionModel().getSelectedItem();
            edtNomAuteur.setText(auteurSelectionner.getNom());
            edtPrenomAuteur.setText(auteurSelectionner.getPrenom());
            mmoResumeAuteur.setText(auteurSelectionner.getResume());
        }
    }

    public void modifierAuteur() throws SQLException {
        if (!tblAuteur.getSelectionModel().isEmpty()) {
            Auteur auteur = tblAuteur.getSelectionModel().getSelectedItem();
            Auteur auteurSelectionner = new Auteur(auteur.getIdAuteur(), edtNomAuteur.getText(), edtPrenomAuteur.getText(), mmoResumeAuteur.getText());
            if (!auteur.equals(auteurSelectionner)) {
                auteurDao.updateAuteur(auteurSelectionner);
                notifBuilder("Opération réussie",
                        "Votre opération de modifier l'auteur' " + auteur.getNom() + " a réussie.",
                        "/Images/checked.png");
                nettoyageScene();
            }
        } else {
            notifBuilder("Attention",
                    "Il faut sélectionner un client pour pouvoir le modifier.",
                    "/Images/warning.png");
        }

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
    public void vider(){
        viderChamps();
    }

}
