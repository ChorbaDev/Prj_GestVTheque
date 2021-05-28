package Controlleur;

import DAO.CommandeDAO;
import DAO.CommandeDAOImpl;
import Modele.Date;
import Modele.Produit;
import Modele.ProduitPanier;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
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
    private Label montantTotale;

    @FXML
    private Label reduction;
    @FXML private JFXButton modifier;
    @FXML private JFXButton retirer;
    @FXML
    private JFXToggleButton toggleFacture;
    @FXML
    private JFXToggleButton toggleDispo;
    @FXML
    private JFXDatePicker date;

    @FXML private TableView<ProduitPanier> tablePanier;
    @FXML private TableColumn<ProduitPanier, String> colPdPn;
    @FXML private TableColumn<ProduitPanier, Integer> colQtPn;
    @FXML private TableColumn<ProduitPanier, Integer> colDureePn;
    @FXML private TableColumn<ProduitPanier, Double> colPrixPn;

    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, Integer> colIdPD;
    @FXML private TableColumn<Produit, String> colTypePD;
    @FXML private TableColumn<Produit, String> colTitrePD;
    @FXML private TableColumn<Produit, Double> colTarifPD;
    @FXML private TableColumn<Produit, Integer> colStockPD;
    @FXML private TableColumn<Produit, ImageView> colPhotoPD;

    ObservableList<String> comboClients = FXCollections.observableArrayList();
    ObservableList<Produit> listeProduits = FXCollections.observableArrayList();
    ObservableList<ProduitPanier> listePanier = FXCollections.observableArrayList();

    //
    private int quantiteMax;
    private CommandeDAO commandeDAO;
    Boolean clientFidele=false;
    //


    @FXML
    void validerPanier(ActionEvent event) throws SQLException, IOException {
        String str=comboClient.getSelectionModel().getSelectedItem();
        Integer idClient=Integer.valueOf(str.substring(0,str.indexOf(' ')) );
        commandeDAO.insertCommande(idClient);
        commandeDAO.insertConcerne(listePanier);
        commandeDAO.modifierStock(listePanier);
        notifBuilder("Opération réussie",
                "cette commande est éffectué avec succès.",
                "/Images/checked.png");
        viderRemplirListeProduits();
        ajoutInfoPanier.setDisable(true);

    }

    public void modifierPanier(){
        int i=tablePanier.getSelectionModel().getSelectedIndex();
        ProduitPanier pp=listePanier.get(i);
        int qt=spinnerQuantite.getValue();
        pp.setQuantite(qt);
        tablePanier.getItems().get(i).setQuantite(spinnerQuantite.getValue());
        double tot=tarifJournalier(pp.getTitreProduit())*qt;
        pp.setPrixTotal(tot);
        Date d1=new Date();
        Date d2=new Date(date.getValue().getDayOfMonth(),date.getValue().getMonthValue(),date.getValue().getYear());
        pp.setDuree(Math.abs(d1.difference(d2)));
        tablePanier.getItems().get(i).setDuree(Math.abs(d1.difference(d2)));

        if(clientFidele)
            montantTotale.setText(Double.toString(sommePrixTotal()*0.9)+" €");
        else
            montantTotale.setText(Double.toString(sommePrixTotal())+" €");
    }

    private double tarifJournalier(String titreProduit) {
        double d=0;
        for(int i=0;i<listeProduits.size();i++){
            if(listeProduits.get(i).getTitreProduit().equals(titreProduit)){
                return listeProduits.get(i).getTarifProduit();
            }
        }
        return d;
    }

    @FXML
    void selectionDePanier(MouseEvent event) {
        if(tablePanier.getSelectionModel().getSelectedIndex()!=-1){
            remplirChampsInfos();
            modifier.setDisable(false);
            retirer.setDisable(false);
        }
    }

    private void remplirChampsInfos() {
        ProduitPanier pp=tablePanier.getSelectionModel().getSelectedItem();
        int qt=pp.getQuantite();
        spinnerQuantite.getValueFactory().setValue(qt);

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,pp.getDuree());
        LocalDate d= LocalDate.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
        date.setValue(d);
    }

    @FXML
    void retirerProduit(ActionEvent event) {
            int i=tablePanier.getSelectionModel().getSelectedIndex();
            tablePanier.getItems().remove(i);
            listePanier.remove(i);
            modifier.setDisable(true);
            retirer.setDisable(true);
            if(clientFidele)
                montantTotale.setText(Double.toString(sommePrixTotal()*0.9)+" €");
            else
                montantTotale.setText(Double.toString(sommePrixTotal())+" €");
        }

    public void ajoutAuPanier(){
        Produit pd=tableProduits.getSelectionModel().getSelectedItem();
        Date d1=new Date();
        Date d2=new Date(date.getValue().getDayOfMonth(),date.getValue().getMonthValue(),date.getValue().getYear());

        String titrePd=pd.getTitreProduit();
        int qt= spinnerQuantite.getValue();
        int duree=Math.abs(d2.difference(d1));
        double prixTot=qt*pd.getTarifProduit();

        ProduitPanier pp=new ProduitPanier(titrePd,qt,duree,prixTot);
        if(existeDansLaListe(titrePd)){
            incrementerProduitPanier(titrePd,pd.getTarifProduit());
        }
        else{
            if(qt>0 && qt<=quantiteMax){
                listePanier.add(pp);
                tablePanier.getItems().add(pp);
            }
            else{
                notifBuilder("Attention",
                        "Il faut saisir une quantite valide.",
                        "/Images/warning.png");
            }
        }
        if(clientFidele)
            montantTotale.setText(Double.toString(sommePrixTotal()*0.9)+" €");
        else
            montantTotale.setText(Double.toString(sommePrixTotal())+" €");

    }

    private boolean existeDansLaListe(String titrePd) {
        for(int i=0;i<listePanier.size();i++){
            if(listePanier.get(i).getTitreProduit().equals(titrePd))
                return true;
        }
        return false;
    }

    private void incrementerProduitPanier(String titrePd, double tarifProduit) {
        for(int i=0;i<listePanier.size();i++){
            if(listePanier.get(i).getTitreProduit().equals(titrePd)){
                int qt=listePanier.get(i).getQuantite();
                int sp=spinnerQuantite.getValue();
                if(qt>0 && (qt+sp)<=quantiteMax){
                    listePanier.get(i).setQuantite(qt+sp);
                    listePanier.get(i).setPrixTotal((qt+sp)*tarifProduit);
                }
                else{
                    notifBuilder("Attention",
                            "Il faut saisir une quantite valide.",
                            "/Images/warning.png");
                }
            }

        }
    }

    private double sommePrixTotal() {
        double som=0;
        for(int i=0;i<listePanier.size();i++){
            som+=listePanier.get(i).getPrixTotal();
        }
        som= (double) Math.round(som * 100) / 100;
        return som;
    }

    public void notifBuilder(String titre, String texte, String pathImg) {
        Image img = new Image(pathImg);
        Notifications notifBuilder = Notifications.create()
                .title(titre)
                .text(texte)
                .graphic(new javafx.scene.image.ImageView(img))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
        notifBuilder.darkStyle();
        notifBuilder.show();
    }
    @FXML
    public void disponible(ActionEvent event) throws SQLException, IOException {
        ajoutInfoPanier.setDisable(true);
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
        ajoutInfoPanier.setDisable(true);
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
        ajoutInfoPanier.setDisable(true);
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
        ajoutInfoPanier.setDisable(true);
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
        ajoutInfoPanier.setDisable(true);
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
        ajoutInfoPanier.setDisable(true);
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
    void annulerPanier(ActionEvent event) {
        listePanier.clear();
        tablePanier.getItems().clear();
        modifier.setDisable(true);
        retirer.setDisable(true);
        montantTotale.setText("0 €");
    }
    public void clientChoisi() throws SQLException {
        if(comboClient.getSelectionModel().getSelectedIndex()!=-1){
            partiePanier.setDisable(false);
            retirer.setDisable(true);
        }
        String str=comboClient.getSelectionModel().getSelectedItem();
        Integer idClient=Integer.valueOf(str.substring(0,str.indexOf(' ')) );
        str="0 %";
        clientFidele=commandeDAO.trouverFidelite(idClient);

        if(clientFidele){
            Double d=sommePrixTotal()*0.9;
            montantTotale.setText(Double.toString(d)+" €");
            str="10 %";
        }
        else{
            montantTotale.setText(Double.toString(sommePrixTotal())+" €");
        }
        reduction.setText(str);


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
            ajoutInfoPanier.setDisable(false);
            modifier.setDisable(true);

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
        colPdPn.setCellValueFactory(new PropertyValueFactory<ProduitPanier,String>("titreProduit"));
        colQtPn.setCellValueFactory(new PropertyValueFactory<ProduitPanier,Integer>("quantite"));
        colDureePn.setCellValueFactory(new PropertyValueFactory<ProduitPanier,Integer>("duree"));
        colPrixPn.setCellValueFactory(new PropertyValueFactory<ProduitPanier,Double>("prixTotal"));
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
