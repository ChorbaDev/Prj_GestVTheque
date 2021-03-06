package Controlleur;

import DAO.AuteurDAOImpl;
import Modele.Auteur;
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
import java.sql.SQLException;
import java.util.ResourceBundle;


public class EcouteurAuteur implements Initializable {
    private final ObservableList<Auteur> obList = FXCollections.observableArrayList();
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;
    private AuteurDAOImpl auteurDao;
    /*en rapport avec la liste d'auteur*/
    @FXML
    private TableView<Auteur> tblAuteur;
    @FXML
    private TableColumn<Auteur, Integer> colIdAuteur;
    @FXML
    private TableColumn<Auteur, String> colNomAuteur;
    @FXML
    private TableColumn<Auteur, String> colPrenomAuteur;
    @FXML
    private TableColumn<Auteur, Integer> colNbLivresAuteur;
    /*champs en relation avec l'auteur*/
    @FXML
    private JFXTextField edtNomAuteur;
    @FXML
    private JFXTextField edtPrenomAuteur;
    @FXML
    private JFXTextArea mmoResumeAuteur;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        colIdAuteur.setCellValueFactory(new PropertyValueFactory<Auteur, Integer>("idAuteur"));
        colNomAuteur.setCellValueFactory(new PropertyValueFactory<Auteur, String>("nom"));
        colPrenomAuteur.setCellValueFactory(new PropertyValueFactory<Auteur, String>("prenom"));
        colNbLivresAuteur.setCellValueFactory(new PropertyValueFactory<Auteur, Integer>("nbLivres"));
        tblAuteur.setItems(obList);
        try {
            auteurDao = new AuteurDAOImpl();
            remplirLaListe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * retourner au sceneBienvenue
     *
     * @param e
     * @throws IOException
     */
    public void retour(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneBienvenue.fxml";
        basculeScene(e, path);
    }

    /**
     * @param e
     * @param pathURL
     * @throws IOException
     */
    public void basculeScene(ActionEvent e, String pathURL) throws IOException
    {
        root = FXMLLoader.load(getClass().getResource(pathURL));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * nettoyage de la scene
     *
     * @throws SQLException
     */
    private void nettoyageScene() throws SQLException
    {
        viderChamps();
        viderListe();
        remplirLaListe();
    }

    /**
     * remplir la liste des auteurs
     *
     * @throws SQLException
     */
    private void remplirLaListe() throws SQLException
    {
        auteurDao.remplirListeAuteur(obList);
    }

    /**
     * vider les champs du texte
     */
    private void viderChamps()
    {
        edtNomAuteur.setText("");
        edtPrenomAuteur.setText("");
        mmoResumeAuteur.setText("");
    }

    /**
     * vider la liste des auteurs
     */
    private void viderListe()
    {
        tblAuteur.getItems().clear();
    }

    /**
     * ajouter l'auteur ?? la bdd et dans la listView aussi
     *
     * @throws SQLException
     */
    public void ajoutAuteur() throws SQLException
    {
        Auteur auteur = new Auteur(edtNomAuteur.getText().trim(), edtPrenomAuteur.getText().trim(), mmoResumeAuteur.getText().trim());
        if (auteurDao.existenceAuteur(auteur)) {
            notifBuilder("Attention",
                    "l'auteur " + edtNomAuteur.getText() + " existe d??j?? dans la base de donn??es.",
                    "/Images/warning.png");
        } else if (validationDesChamps()) {
            auteurDao.insertAuteur(auteur);
            notifBuilder("Op??ration r??ussie",
                    "Votre op??ration pour ajouter l'auteur " + edtNomAuteur.getText() + " a ??t?? ??ffectu?? avec succ??s.",
                    "/Images/checked.png");
            nettoyageScene();
        } else {
            notifBuilder("Attention",
                    "il faut remplir tous les champs.",
                    "/Images/warning.png");
        }
    }

    /**
     * @return vrai si tous les champs sont remplis sinon faux
     */
    private boolean validationDesChamps()
    {
        return !edtNomAuteur.getText().isEmpty() && !edtPrenomAuteur.getText().isEmpty() && !mmoResumeAuteur.getText().isEmpty();
    }

    /**
     * remplir les champs avec les infos de l'auteur selectionn?? sinon rien faire
     *
     * @throws SQLException
     * @throws IOException
     */
    public void selectionAuteur() throws SQLException, IOException
    {
        viderChamps();
        if (!tblAuteur.getSelectionModel().isEmpty()) {
            Auteur auteurSelectionner = tblAuteur.getSelectionModel().getSelectedItem();
            edtNomAuteur.setText(auteurSelectionner.getNom());
            edtPrenomAuteur.setText(auteurSelectionner.getPrenom());
            mmoResumeAuteur.setText(auteurSelectionner.getResume());
        }
    }

    /**
     * modifier l'auteur selectionn?? en utilisant les champs du texte remplis
     *
     * @throws SQLException
     */
    public void modifierAuteur() throws SQLException
    {
        if (!tblAuteur.getSelectionModel().isEmpty()) {
            Auteur auteur = tblAuteur.getSelectionModel().getSelectedItem();
            Auteur auteurSelectionner = new Auteur(auteur.getIdAuteur(), edtNomAuteur.getText(), edtPrenomAuteur.getText(), mmoResumeAuteur.getText());
            if (!auteur.equals(auteurSelectionner) && validationDesChamps()) {
                auteurDao.updateAuteur(auteurSelectionner);
                notifBuilder("Op??ration r??ussie",
                        "Votre op??ration pour modifier l'auteur' " + auteur.getNom() + " a r??ussie.",
                        "/Images/checked.png");
                nettoyageScene();
            }
        } else {
            notifBuilder("Attention",
                    "Il faut s??lectionner un client pour pouvoir le modifier.",
                    "/Images/warning.png");
        }

    }

    /**
     * constructions de la notification ?? afficher
     *
     * @param titre
     * @param texte
     * @param pathImg
     */
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

    public void vider()
    {
        viderChamps();
    }

}
