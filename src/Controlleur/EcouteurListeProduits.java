package Controlleur;

import DAO.ProduitDAOImpl;
import Modele.*;
import com.jfoenix.controls.*;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EcouteurListeProduits implements Initializable {
    //
    @FXML private TableView<Produit> table;
    @FXML private TableColumn<Produit, String> colType;
    @FXML private TableColumn<Produit, Integer> colId;
    @FXML private TableColumn<Produit, String> colTitre;
    @FXML private TableColumn<Produit, Integer> colStock;
    @FXML private TableColumn<Produit, Float> colTarif;
    ObservableList<Produit> obList= FXCollections.observableArrayList();
    ObservableList<String> comboList = FXCollections.observableArrayList();
    ObservableList<String> comboListRealisateur = FXCollections.observableArrayList();
    ObservableList<String> comboListAuteur = FXCollections.observableArrayList();
    ObservableList<String> comboListAnnee = FXCollections.observableArrayList();
    ObservableList<String> comboListLangue = FXCollections.observableArrayList();
    //
    @FXML private JFXComboBox<String> typeProduit;
    @FXML private JFXTextField nomProduit;
    @FXML private JFXTextField stockProduit;
    @FXML private JFXTextField tarifProduit;
    @FXML private ImageView image;
    @FXML private JFXComboBox<String> anneeSortie;
    @FXML private JFXComboBox<String> langueDictionnaire;
    @FXML private JFXTextField dureeSupportN;
    @FXML private JFXTextField nbPagesDocument;
    @FXML private JFXComboBox<String> realDVD;
    @FXML private JFXComboBox<String> auteurLivre;
    //
    private ProduitDAOImpl produitDao;
    private Boolean uneImageEstSelectionner=false;
    private FileInputStream fis=new FileInputStream(new File("src/Images/pasdispo.png"));

    public EcouteurListeProduits() throws FileNotFoundException {
    }


    private boolean champsNonVide() {
        Boolean b1= (typeProduit.getSelectionModel().getSelectedIndex()!=-1) && (!stockProduit.getText().isEmpty()) && (!nomProduit.getText().isEmpty()) && (!tarifProduit.getText().isEmpty());
        try{
            int i=Integer.parseInt(stockProduit.getText());
            Double j=Double.parseDouble(tarifProduit.getText());
        }
        catch (Exception e){
            notifBuilder("Attention",
                    "Vérifier les champs de tarif et stock.",
                    "/Images/warning.png");
            return false;
        }
       if(langueDictionnaire.isVisible()) b1=b1 && (langueDictionnaire.getSelectionModel().getSelectedIndex()!=-1);
       if(auteurLivre.isVisible()) b1=b1 && (auteurLivre.getSelectionModel().getSelectedIndex()!=-1);
        if(anneeSortie.isVisible()) b1=b1 && (anneeSortie.getSelectionModel().getSelectedIndex()!=-1);
        if(realDVD.isVisible()) b1=b1 && (realDVD.getSelectionModel().getSelectedIndex()!=-1);
        if(nbPagesDocument.isVisible()){
            b1=b1 && !nbPagesDocument.getText().isEmpty();
            try{
                int i=Integer.valueOf(nbPagesDocument.getText());
            }catch (Exception e){
                notifBuilder("Attention",
                        "Vérifier le champ de nombre de pages.",
                        "/Images/warning.png");
                return false;
            }
        }
        if(dureeSupportN.isVisible()) {
            b1=b1 && !dureeSupportN.getText().isEmpty();
            try{
                int j=Integer.valueOf(dureeSupportN.getText());
            }catch (Exception e){
                notifBuilder("Attention",
                        "Vérifier le champs de la duree.",
                        "/Images/warning.png");
                return false;
            }
        }
       return b1;
    }
    @FXML
    void ajouterProduit(ActionEvent event) throws SQLException, IOException {
        if(champsNonVide()){
            Produit pd=new Produit(nomProduit.getText(),Integer.valueOf(stockProduit.getText()),Double.valueOf(tarifProduit.getText()),typeProduit.getSelectionModel().getSelectedItem());
            PreparedStatement statement= produitDao.ajouterProduit(pd);
            String ch;
            if(anneeSortie.getSelectionModel().getSelectedIndex()==-1)
                statement.setNull(5,Types.NULL);
            else{
                ch=anneeSortie.getSelectionModel().getSelectedItem();
                statement.setInt(5, Integer.parseInt(ch));
            }
            if(langueDictionnaire.getSelectionModel().getSelectedIndex()==-1)
                statement.setNull(6,Types.NULL);
            else{
                ch=langueDictionnaire.getSelectionModel().getSelectedItem();
                statement.setString(6,ch);
            }
            if(dureeSupportN.getText().isEmpty())
                statement.setNull(7,Types.NULL);
            else{
                statement.setInt(   7,Integer.valueOf(dureeSupportN.getText()));
            }
            if(nbPagesDocument.getText().isEmpty())
                statement.setNull(8,Types.NULL);
            else{
                statement.setInt(   8,Integer.valueOf(nbPagesDocument.getText()));
            }
            if(realDVD.getSelectionModel().getSelectedIndex()==-1)
                statement.setNull(9,Types.NULL);
            else{
                ch=realDVD.getSelectionModel().getSelectedItem();
                statement.setInt(9,Integer.valueOf(ch.substring(0,ch.indexOf(" "))));
            }
            if(auteurLivre.getSelectionModel().getSelectedIndex()==-1)
                statement.setNull(10,Types.NULL);
            else{
                ch=auteurLivre.getSelectionModel().getSelectedItem();
                statement.setInt(10,Integer.valueOf(ch.substring(0,ch.indexOf(" "))));
            }

            if(fis.available()<=32)
                fis=new FileInputStream(new File("src/Images/pasdispo.png"));
            statement.setBinaryStream(11,fis);
            uneImageEstSelectionner=false;
            statement.executeUpdate();
            statement.close();
            notifBuilder("Opération réussie",
                    "Votre opération d'ajouter le produit "+nomProduit.getText()+" a réussie.",
                    "/Images/checked.png");
            nettoyageScene();
            partieInvisible();
        }
        else{
            notifBuilder("Attention",
                    "Pour pouvoir ajouter un produit, il faut remplir tout les champs.",
                    "/Images/warning.png");
        }

    }




    @FXML
    public void afficheChampsType(ActionEvent e){
    if(e.getSource()==typeProduit){
        partieInvisible();
        int i=typeProduit.getSelectionModel().getSelectedIndex();
        switch (i){
            case 0: case 2: case 4: case 5:
                nbPagesDocument.setVisible(true);
                if(i==2)
                    langueDictionnaire.setVisible(true);
                else
                    auteurLivre.setVisible(true);
                break;
            case 1: case 3:
                dureeSupportN.setVisible(true);
                if(i==1)
                    anneeSortie.setVisible(true);
                else
                    realDVD.setVisible(true);
                break;
            default:
                break;
        }
    }


    }
    private void modifierProduitSelectionner(Produit pd) throws SQLException {
        PreparedStatement statement=produitDao.modifierProduitSelectionner(pd);
        String ch;
        if(anneeSortie.getSelectionModel().getSelectedIndex()==-1)
            statement.setNull(5,Types.NULL);
        else{
            ch=anneeSortie.getSelectionModel().getSelectedItem();
            statement.setInt(   5,Integer.valueOf(ch));
        }

        if(langueDictionnaire.getSelectionModel().getSelectedIndex()==-1)
            statement.setNull(6,Types.NULL);
        else{
            ch=langueDictionnaire.getSelectionModel().getSelectedItem();
            statement.setString(6,ch);
        }
            if(dureeSupportN.getText().isEmpty())
                statement.setNull(7,Types.NULL);
            else{
                statement.setInt(   7,Integer.valueOf(dureeSupportN.getText()));
            }

            if(nbPagesDocument.getText().isEmpty())
                statement.setNull(8,Types.NULL);
            else{
                statement.setInt(   8,Integer.valueOf(nbPagesDocument.getText()));
            }

            if(realDVD.getSelectionModel().getSelectedIndex()==-1)
                statement.setNull(9,Types.NULL);
            else{
                ch=realDVD.getSelectionModel().getSelectedItem();
                statement.setInt(9,Integer.valueOf(ch.substring(0,ch.indexOf(" "))));
            }
            if(auteurLivre.getSelectionModel().getSelectedIndex()==-1)
                statement.setNull(10,Types.NULL);
            else{
                ch=auteurLivre.getSelectionModel().getSelectedItem();
                statement.setInt(10,Integer.valueOf(ch.substring(0,ch.indexOf(" "))));
            }
            if(uneImageEstSelectionner){
                statement.setBinaryStream(11,fis);
                uneImageEstSelectionner=false;
            }
            else{
                InputStream is=inputProduit(pd);
                statement.setBinaryStream(11,is);
            }
            statement.setString(12,"Document");
            statement.executeUpdate();
            statement.close();
            nettoyageScene();
    }

    private InputStream inputProduit(Produit pd) throws SQLException {
        ResultSet res=produitDao.inputProduit(pd.getIdProduit());
        res.next();
        InputStream is=res.getBinaryStream("photo");
        return is;
    }

    @FXML
    void supprimerProduit(ActionEvent event) throws SQLException {
        if(!table.getSelectionModel().isEmpty()) {
            Produit produitSupprimer=table.getSelectionModel().getSelectedItem();
            produitDao.supprimerProduit(produitSupprimer);
            notifBuilder("Opération réussie",
                    "Votre opération de suppression du " + nomProduit.getText() + " est éffectué avec succès.",
                    "/Images/checked.png");
            nettoyageScene();
        }
        else{
            notifBuilder("Attention",
                    "Il faut sélectionner un produit pour pouvoir le supprimer.",
                    "/Images/warning.png");
        }
    }
    private void viderListe() {
        table.getItems().clear();
    }
    private void nettoyageScene() throws SQLException {
        viderListe();
        partieInvisible();
        viderChamps();
        remplirLaListe();
    }

    private void viderChamps() {
        image.setImage(new Image("/Images/pasdispo.png"));
        nbPagesDocument.setText("");
        dureeSupportN.setText("");
        tarifProduit.setText("");
        stockProduit.setText("");
        nomProduit.setText("");
        langueDictionnaire.setValue(null);
        auteurLivre.setValue(null);
        anneeSortie.setValue(null);
        realDVD.setValue(null);
        typeProduit.setValue(null);
    }
    @FXML
    void selectProduit(MouseEvent event) throws SQLException, IOException {
        viderChamps();
        if(!table.getSelectionModel().isEmpty()){
            Produit produitSelectionner=table.getSelectionModel().getSelectedItem();
            selectionImage(produitSelectionner);
            nomProduit.setText(produitSelectionner.getTitreProduit());
            stockProduit.setText(Integer.toString(produitSelectionner.getStockProduit()));
            tarifProduit.setText(Double.toString(produitSelectionner.getTarifProduit()));
            modifType  (produitSelectionner.getIdProduit());
            modifLangue(produitSelectionner.getIdProduit());
            modifAuteur(produitSelectionner.getIdProduit());
            modifReal  (produitSelectionner.getIdProduit());
            modifAnnee (produitSelectionner.getIdProduit());
            modifNbpages (produitSelectionner.getIdProduit());
            modifDuree (produitSelectionner.getIdProduit());
            partieInvisible();
            switch(produitSelectionner.getTypeProduit()){
                case "BD" : case "Manuel Scolaire" : case "Roman" : case "Dictionnaire":
                    nbPagesDocument.setVisible(true) ;
                    if(produitSelectionner.getTypeProduit().equals("Dictionnaire")){
                        langueDictionnaire.setVisible(true);
                    }
                    else{
                        auteurLivre.setVisible(true);
                    }
                    break;
                default:
                    dureeSupportN.setVisible(true);
                    if(produitSelectionner.getTypeProduit().equals("CD")){
                        anneeSortie.setVisible(true);
                    }
                    else{
                        realDVD.setVisible(true);
                    }
                    break;
            }
        }
    }

    private void modifDuree(int idProduit) throws SQLException {
        ResultSet res=produitDao.modifDuree(idProduit);
        if(res.next())
            dureeSupportN.setText((Integer.toString(res.getInt("duree"))));
    }

    private void modifNbpages(int idProduit) throws SQLException {
        ResultSet res=produitDao.modifNbpages(idProduit);
        if(res.next())
        nbPagesDocument.setText((Integer.toString(res.getInt("nbPages"))));
    }
    private void modifType(int idProduit) throws SQLException {
        ResultSet res=produitDao.modifType(idProduit);
        if(res.next())
        typeProduit.setValue(res.getString("type"));
    }
    private void modifLangue(int idProduit) throws SQLException {
        ResultSet res=produitDao.modifLangue(idProduit);
        if(res.next())
        langueDictionnaire.setValue(res.getString("langue"));
    }
    private void modifAuteur(int idProduit) throws SQLException {
        ResultSet res=produitDao.modifAuteur(idProduit);
        if(res.next())
        auteurLivre.setValue(res.getString("auteur")+" "+res.getString("nomAuteur"));
    }
    private void modifReal(int idProduit) throws SQLException {
        ResultSet res=produitDao.modifReal(idProduit);
        if(res.next())
        realDVD.setValue(res.getString("realisateur")+" "+res.getString("nomReal"));
    }
    private void modifAnnee(int idProduit) throws SQLException {
        ResultSet res=produitDao.modifAnnee(idProduit);
        if(res.next())
        anneeSortie.setValue(res.getString("anneeSortie"));
    }

    private void selectionImage( Produit pd) throws SQLException, IOException {
        image.setImage(imageProduit(pd));
    }
    public Image imageProduit(Produit pd) throws SQLException, IOException {
        return produitDao.imageProduit(pd.getIdProduit());
    }
    private void remplirLaListe() throws SQLException {
            produitDao.remplirLaListe(obList);
    }
    private void remplirComboTypes() throws SQLException {
        produitDao.remplirComboTypes(comboList);
    }
    @FXML
    void choisirUneImage(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)","*.JPG");
        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)","*.PNG");
        fc.getExtensionFilters().addAll(ext1,ext2);
        File fichierSelect=fc.showOpenDialog(null);
        if(fichierSelect!=null){
            fis=new FileInputStream(fichierSelect);
            image.setImage(new Image(fichierSelect.toURI().toString()));
            uneImageEstSelectionner=true;
        }

    }

    @FXML
    void modifierProduit(ActionEvent event) throws SQLException {
        if(!table.getSelectionModel().isEmpty()){
            if(champsNonVide()){
                Produit prod=table.getSelectionModel().getSelectedItem();
                Produit produitSelectionner=new Produit(prod.getIdProduit(), nomProduit.getText(),Integer.valueOf(stockProduit.getText()),Double.valueOf(tarifProduit.getText()),typeProduit.getSelectionModel().getSelectedItem());
                modifierProduitSelectionner(produitSelectionner);
                notifBuilder("Opération réussie",
                        "Votre opération de modifier le produit "+prod.getTitreProduit()+" a réussie.",
                        "/Images/checked.png");
                partieInvisible();
            }
           else{
                notifBuilder("Attention",
                        "Vérifier vos champs.",
                        "/Images/warning.png");
            }

        }
        else{
            notifBuilder("Attention",
                    "Il faut sélectionner un produit pour pouvoir le modifier.",
                    "/Images/warning.png");
        }
    }

    public void notifBuilder(String titre,String texte,String pathImg){
        Image img=new Image(pathImg);
        Notifications notifBuilder=Notifications.create()
                .title(titre)
                .text(texte)
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
        notifBuilder.darkStyle();
        notifBuilder.show();
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

    @FXML
    void viderImage(ActionEvent event) throws FileNotFoundException {
        File file=new File("src/Images/pasdispo.png");
        fis=new FileInputStream(file);
        image.setImage(new Image("/Images/pasdispo.png"));
        uneImageEstSelectionner=true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partieInvisible();
        colType.setCellValueFactory (new PropertyValueFactory<Produit,String>("typeProduit"));
        colId.setCellValueFactory   (new PropertyValueFactory<Produit,Integer>("idProduit"));
        colTitre.setCellValueFactory(new PropertyValueFactory<Produit,String>("titreProduit"));
        colStock.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("stockProduit"));
        colTarif.setCellValueFactory(new PropertyValueFactory<Produit,Float>("tarifProduit"));
        table.setItems(obList);
        try {
            produitDao=new ProduitDAOImpl();
            remplirLaListe();
            remplirComboTypes();
            remplirComboAuteur();
            remplirComboRealisateur();
            remplirComboAnnee();
            remplirComboLangues();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        typeProduit.setItems(comboList);
        auteurLivre.setItems(comboListAuteur);
        realDVD.setItems(comboListRealisateur);
        anneeSortie.setItems(comboListAnnee);
        langueDictionnaire.setItems(comboListLangue);
    }

    private void remplirComboLangues() {
        comboListLangue.add("Anglais");
        comboListLangue.add("Arabe");
        comboListLangue.add("Français");
        comboListLangue.add("Espoagnol");
        comboListLangue.add("Allemand");
        comboListLangue.add("Russe");
        comboListLangue.add("Japonais");
    }

    private void remplirComboAnnee(){
        for(int i=1980;i<=2021;i++){
            comboListAnnee.add(Integer.toString(i));
        }
    }

    private void remplirComboRealisateur() throws SQLException {
        produitDao.remplirComboRealisateur(comboListRealisateur);
    }

    private void remplirComboAuteur() throws SQLException {
        produitDao.remplirComboAuteur(comboListAuteur);
    }
    private void partieInvisible() {
        langueDictionnaire.setVisible(false);
        auteurLivre.setVisible(false);
        nbPagesDocument.setVisible(false);
        anneeSortie.setVisible(false);
        dureeSupportN.setVisible(false);
        realDVD.setVisible(false);
    }
    public void vider(){
        partieInvisible();
        viderChamps();
    }
}
