package Controlleur;

import Modele.Client;
import DAO.ClientDAOImpl;
import com.jfoenix.controls.JFXCheckBox;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EcouteurListeClients implements Initializable {
    ObservableList<Client> obList = FXCollections.observableArrayList();
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;
    private ClientDAOImpl clientDAO;
    /*Concerne la liste*/
    @FXML
    private TableView<Client> table;
    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colNom;
    @FXML
    private TableColumn<Client, String> colPrenom;
    @FXML
    private TableColumn<Client, String> colMail;
    @FXML
    private TableColumn<Client, Boolean> colFidele;
    /*Concerne la scene*/
    @FXML
    private JFXTextField nom;
    @FXML
    private JFXTextField prenom;
    @FXML
    private JFXCheckBox fidele;
    @FXML
    private JFXTextField mail;
    @FXML
    private ImageView image;
    private FileInputStream fis = new FileInputStream(new File("src/Images/Photo-non-disponible.jpg"));
    private Boolean uneImageEstSelectionner = false;

    public EcouteurListeClients() throws FileNotFoundException {
    }

    public void remplirLaListe() throws SQLException {
        clientDAO.remplirListeClient(obList);
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
        if (!table.getSelectionModel().isEmpty()) {
            Client clientSupprmier = table.getSelectionModel().getSelectedItem();
            clientDAO.supprimerClient(clientSupprmier);
            notifBuilder("Opération réussie",
                    "Votre opération de suppression du " + nom.getText() + " est éffectué avec succès.",
                    "/Images/checked.png");
            nettoyageScene();
        } else {
            notifBuilder("Attention",
                    "Il faut sélectionner un client pour pouvoir le supprimer.",
                    "/Images/warning.png");
        }
    }

    public void ajoutClient() throws SQLException, IOException {

        Client client = new Client(nom.getText().trim(), prenom.getText().trim(), mail.getText().trim(), fidele.isSelected());
        PreparedStatement statement=clientDAO.insertClient(client);
        if (clientDAO.existenceClient(client)) {
            notifBuilder("Attention",
                    "le mail " + mail.getText() + " déja existe dans la base de données.",
                    "/Images/warning.png");
        } else if (validationDesChamps()) {
            if (fis.available() <= 32)
                fis = new FileInputStream(new File("src/Images/pasdispo.png"));
            statement.setBinaryStream(4, fis);
            uneImageEstSelectionner = false;
            statement.executeUpdate();
            statement.close();
            notifBuilder("Opération réussie",
                    "Votre opération d'ajouter le client " + nom.getText() + " est éffectué avec succès.",
                    "/Images/checked.png");
            nettoyageScene();
        } else {
            notifBuilder("Attention",
                    "il faut remplir tout les champs.",
                    "/Images/warning.png");
        }
    }

    private boolean validationDesChamps() {
        return !nom.getText().isEmpty() && !prenom.getText().isEmpty() && !mail.getText().isEmpty();
    }

    public void ChoisirUneImage() throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
        fc.getExtensionFilters().addAll(ext1, ext2);
        File fichierSelect = fc.showOpenDialog(null);
        if (fichierSelect != null) {
            fis = new FileInputStream(fichierSelect);
            image.setImage(new Image(fichierSelect.toURI().toString()));
            uneImageEstSelectionner = true;
        }

    }

    public void viderImage() throws IOException {
        File file = new File("src/Images/Photo-non-disponible.jpg");
        fis = new FileInputStream(file);
        image.setImage(new Image("/Images/Photo-non-disponible.jpg"));
        uneImageEstSelectionner = true;
    }

    public void modifierClient() throws SQLException, IOException {
        if (!table.getSelectionModel().isEmpty()) {
            Client client = table.getSelectionModel().getSelectedItem();
            Client clientSelectionner = new Client(client.getIdClient(), nom.getText(), prenom.getText(), mail.getText(), fidele.isSelected());
            if (!client.equals(clientSelectionner) || uneImageEstSelectionner) {
//                System.out.println(clientSelectionner.toString());
                modifierClientSelectionner(clientSelectionner);
                notifBuilder("Opération réussie",
                        "Votre opération de modifier le client " + client.getNom() + " a réussie.",
                        "/Images/checked.png");
            }
        } else {
            notifBuilder("Attention",
                    "Il faut sélectionner un client pour pouvoir le modifier.",
                    "/Images/warning.png");
        }

    }

    private void modifierClientSelectionner(Client cl) throws SQLException, IOException {
        PreparedStatement statement=clientDAO.modifierClientSelectionner(cl);
        if (uneImageEstSelectionner) {
            statement.setBinaryStream(5, fis);
            uneImageEstSelectionner = false;
        } else {
            InputStream is = inputClient(cl);
            statement.setBinaryStream(5, is);
        }
        statement.executeUpdate();
        statement.close();
        nettoyageScene();
    }

    private InputStream inputClient(Client cl) throws SQLException {
        ResultSet res=clientDAO.inputClient(cl.getIdClient());
        res.next();
        InputStream is = res.getBinaryStream("pdp");
        return is;
    }

    public void selectionClient() throws SQLException, IOException {
        viderChamps();
        if (!table.getSelectionModel().isEmpty()) {
            Client clientSelectionner = table.getSelectionModel().getSelectedItem();
            selectionImage(clientSelectionner);
            nom.setText(clientSelectionner.getNom());
            prenom.setText(clientSelectionner.getPrenom());
            mail.setText(clientSelectionner.getMailClient());
            if (clientSelectionner.isClientFidele()) {
                fidele.setSelected(true);
            }
        }
    }

    private Image imageClient(Client cl) throws SQLException, IOException {
        return clientDAO.imageClient(cl.getIdClient());
    }

    private void selectionImage(Client cl) throws SQLException, IOException {
        image.setImage(imageClient(cl));
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

    private void viderChamps() {
        mail.setText("");
        nom.setText("");
        prenom.setText("");
        image.setImage(new Image("/Images/Photo-non-disponible.jpg"));
        fidele.setSelected(false);
    }

    public void retour(ActionEvent e) throws IOException {
        path = "/Vue/SceneBienvenue.fxml";
        root = FXMLLoader.load(getClass().getResource(path));
        basculeScene(e);
    }

    public void ensembleDesCommandes(ActionEvent e) throws IOException, SQLException {
        if (!table.getSelectionModel().isEmpty()) {
            path = "/Vue/SceneCommandesClient.fxml";
            Client cl = new Client(
                    table.getSelectionModel().getSelectedItem().getIdClient(),
                    table.getSelectionModel().getSelectedItem().getNom(),
                    table.getSelectionModel().getSelectedItem().getPrenom(),
                    table.getSelectionModel().getSelectedItem().getMailClient(),
                    table.getSelectionModel().getSelectedItem().isClientFidele()
            );
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path));
            loader.load();
            EcouteurCommandesClient scene2 = loader.getController();
            Image imgClient = imageClient(cl);
            scene2.getInfos(cl, imgClient);

            root = loader.getRoot();
            basculeScene(e);
        } else {
            notifBuilder("Attention",
                    "Il faut sélectionner un client pour pouvoir afficher ses commandes.",
                    "/Images/warning.png");
        }

    }

    public void basculeScene(ActionEvent e) throws IOException {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<Client, Integer>("idClient"));
        colNom.setCellValueFactory(new PropertyValueFactory<Client, String>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<Client, String>("prenom"));
        colMail.setCellValueFactory(new PropertyValueFactory<Client, String>("mailClient"));
        colFidele.setCellValueFactory(new PropertyValueFactory<Client, Boolean>("clientFidele"));
        table.setItems(obList);
        try {
            clientDAO = new ClientDAOImpl();
            remplirLaListe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void vider() {
        viderChamps();
    }
}
