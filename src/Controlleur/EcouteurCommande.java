package Controlleur;

import DAO.CommandeDAO;
import DAO.CommandeDAOImpl;
import DAO.ProduitDAO;
import Modele.Date;
import Modele.Produit;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXToggleButton;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ResourceBundle;

public class EcouteurCommande implements Initializable {
    @FXML
    private Spinner<Integer> spinnerQuantite;

    @FXML
    private VBox ajoutInfoPanier;
    @FXML
    private AnchorPane partiePanier;
    @FXML
    private JFXComboBox<String> comboClient;

    @FXML
    private TableView<?> tablePanier;

    @FXML
    private Label montantTotale;

    @FXML
    private Label reduction;

    @FXML
    private JFXToggleButton toggleFacture;
    @FXML
    private JFXToggleButton toggleDispo;
    @FXML
    private JFXDatePicker date;
    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, Integer> colIdPD;
    @FXML private TableColumn<Produit, String> colTypePD;
    @FXML private TableColumn<Produit, String> colTitrePD;
    @FXML private TableColumn<Produit, Double> colTarifPD;
    @FXML private TableColumn<Produit, Integer> colStockPD;
    @FXML private TableColumn<Produit, ImageView> colPhotoPD;

    ObservableList<String> comboClients = FXCollections.observableArrayList();
    ObservableList<Produit> listeProduits = FXCollections.observableArrayList();
    //
    private int quantiteMax;
    private CommandeDAO commandeDAO;
    //




    @FXML
    void retirerProduit(ActionEvent event) {

    }

    @FXML
    void selectionDePanier(MouseEvent event) {

    }
    @FXML
    public void disponible(ActionEvent event) throws SQLException, IOException {
        if(toggleDispo.isSelected()==true){
            Iterator it=listeProduits.iterator();
            while(it.hasNext()){
                Produit p= (Produit) it.next();
                if(p.getStockProduit()==0) {
                    it.remove();
                }
            }
            tableProduits.setItems(listeProduits);
        }
        else{
            viderRemplirListeProduits();
        }

    }
    public void triBD() throws SQLException, IOException {
        viderRemplirListeProduits();
        Iterator it=listeProduits.iterator();
        while(it.hasNext()){
            Produit p= (Produit) it.next();
            if(!p.getTypeProduit().equals("BD")) {
                it.remove();
            }
        }
        tableProduits.setItems(listeProduits);
    }
    @FXML
    void triCD(ActionEvent event) throws SQLException, IOException {
        viderRemplirListeProduits();
        Iterator it=listeProduits.iterator();
        while(it.hasNext()){
            Produit p= (Produit) it.next();
            if(!p.getTypeProduit().equals("CD")) {
                it.remove();
            }
        }
        tableProduits.setItems(listeProduits);
    }

    @FXML
    void triDVD(ActionEvent event) throws SQLException, IOException {
        viderRemplirListeProduits();
        Iterator it=listeProduits.iterator();
        while(it.hasNext()){
            Produit p= (Produit) it.next();
            if(!p.getTypeProduit().equals("DVD")) {
                it.remove();
            }
        }
        tableProduits.setItems(listeProduits);
    }

    @FXML
    void triDictionnaire(ActionEvent event) throws SQLException, IOException {
        viderRemplirListeProduits();
        Iterator it=listeProduits.iterator();
        while(it.hasNext()){
            Produit p= (Produit) it.next();
            if(!p.getTypeProduit().equals("Dictionnaire")) {
                it.remove();
            }
        }
        tableProduits.setItems(listeProduits);
    }

    @FXML
    void triManuel(ActionEvent event) throws SQLException, IOException {
        viderRemplirListeProduits();
        Iterator it=listeProduits.iterator();
        while(it.hasNext()){
            Produit p= (Produit) it.next();
            if(!p.getTypeProduit().equals("Manuel Scolaire")) {
                it.remove();
            }
        }
        tableProduits.setItems(listeProduits);
    }

    private void viderRemplirListeProduits() throws SQLException, IOException {
        tableProduits.getItems().clear();
        listeProduits.clear();
        commandeDAO.remplirListeProduits(listeProduits);
        tableProduits.setItems(listeProduits);
    }

    @FXML
    void triRoman(ActionEvent event) throws SQLException, IOException {
        viderRemplirListeProduits();
        Iterator it=listeProduits.iterator();
        while(it.hasNext()){
            Produit p= (Produit) it.next();
            if(!p.getTypeProduit().equals("Roman")) {
                it.remove();
            }
        }
        tableProduits.setItems(listeProduits);
    }

    @FXML
    void validerPanier(ActionEvent event) {

    }

    @FXML
    void annulerPanier(ActionEvent event) {
        tablePanier.getItems().clear();
    }
    public void clientChoisi(){
        if(comboClient.getSelectionModel().getSelectedIndex()!=-1){
            ajoutInfoPanier.setDisable(false);
            partiePanier.setDisable(false);
        }
    }

    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;
    @FXML
    void retour(ActionEvent e) throws IOException {
        path="/Vue/SceneBienvenue.fxml";
        root = FXMLLoader.load(getClass().getResource(path));
        basculeScene(e);
    }
    public void basculeScene(ActionEvent e) throws IOException {
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void spinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, quantiteMax, 1);
        spinnerQuantite.setValueFactory(valueFactory);
    }
    public void selectProduit(){
        if(tableProduits.getSelectionModel().getSelectedIndex()!=-1){
            Produit p=tableProduits.getSelectionModel().getSelectedItem();
            quantiteMax=p.getStockProduit();
            spinner();
        }
    }
    private void DisableChamps() {
        ajoutInfoPanier.setDisable(true);
        partiePanier.setDisable(true);
    }
    public void changeDate(){
        Date d1=new Date(date.getValue().getDayOfMonth(),date.getValue().getMonthValue(),date.getValue().getYear() );
        Date d2=new Date();
        if(!d1.superieurEgale(d2))
           date.setValue(LocalDate.now());
    }
    private void setItemsProduits() {
        colIdPD.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("idProduit"));
        colTitrePD.setCellValueFactory(new PropertyValueFactory<Produit,String>("titreProduit"));
        colStockPD.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("stockProduit"));
        colTarifPD.setCellValueFactory(new PropertyValueFactory<Produit,Double>("tarifProduit"));
        colPhotoPD.setCellValueFactory(new PropertyValueFactory<Produit,ImageView>("imageProduit"));
        colTypePD.setCellValueFactory(new PropertyValueFactory<Produit,String>("typeProduit"));
    }

    private void setItemsPanier() {
    }

    private void remplirTableProduits() throws SQLException, IOException {
        commandeDAO.remplirListeProduits(listeProduits);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DisableChamps();
        setItemsProduits();
        setItemsPanier();
        date.setValue(LocalDate.now());
        try{
            commandeDAO=new CommandeDAOImpl();
            remplirTableProduits();
            remplirComboClient();
        }catch(SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        tableProduits.setItems(listeProduits);

    }
    private void remplirComboClient() throws SQLException {
        commandeDAO.remplirComboClient(comboClients);
        comboClient.setItems(comboClients);
    }
}
