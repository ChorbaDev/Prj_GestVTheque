package Controlleur;

import DAO.CommandeDAO;
import DAO.CommandeDAOImpl;
import Modele.Date;
import Modele.Produit;
import Modele.ProduitPanier;


import be.quodlibet.boxable.*;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.line.LineStyle;

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
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.jbig2.segments.Table;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.controlsfx.control.Notifications;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ResourceBundle;

public class EcouteurCommande implements Initializable {
    @FXML private Spinner<Integer> spinnerQuantite;
    @FXML private VBox ajoutInfoPanier;
    @FXML private AnchorPane partiePanier;
    @FXML private JFXComboBox<String> comboClient;

    @FXML private Label montantTotale;
    @FXML private Label reduction;

    @FXML private JFXButton modifier;
    @FXML private JFXButton retirer;
    @FXML private JFXButton valid;
    @FXML private JFXToggleButton toggleFacture;
    @FXML private JFXToggleButton toggleDispo;
    @FXML private JFXDatePicker date;

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

    /**
     * @param event
     * @throws SQLException
     * @throws IOException
     */

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
        if(toggleFacture.isSelected()){
            facture();
        }
    }

    private void facture() throws IOException
    {
        String outputFileName = "Facture.pdf";

        // Create a new font object selecting one of the PDF base fonts
        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
        PDFont fontMono = PDType1Font.COURIER;

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        // PDRectangle.LETTER and others are also possible
        // rect can be used to get the page width and height
        document.addPage(page);

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream cos = new PDPageContentStream(document, page);

        //Dummy Table
        float margin = 50;
        int spaceBetweenTables = 50;
// starting y position is whole page height subtracted by top and bottom margin
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin)-spaceBetweenTables;
// we want table across whole page width (subtracted by left and right margin ofcourse)
        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);



        boolean drawContent = true;
        float yStart = yStartNewPage;
        float bottomMargin = 70;
        // y position is your coordinate of top left corner of the table
        float yPosition = 770;


        BaseTable tableTitre = new BaseTable(yPosition, yStart,
                bottomMargin, tableWidth, margin, document, page, true, drawContent);

        // the parameter is the row height
        Row<PDPage> headerRow = tableTitre.createRow(50);
        // the first parameter is the cell width
        Cell<PDPage> cell = headerRow.createCell(100, "Logo");
        cell.setFont(fontBold);
        cell.setFontSize(20);
        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        // border style
        cell.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        tableTitre.addHeaderRow(headerRow);

        Row<PDPage> row = tableTitre.createRow(20);
        cell = row.createCell(50, "FACTURE");
        cell.setFontSize(24);
        cell.setFont(fontBold);
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setAlign(HorizontalAlignment.CENTER);
        cell = row.createCell(50, "VStore<br> IUT de Metz<br> Ile du Saulcy<br> BP 10628<br> 57045 Metz cedex 01");
        cell.setFontSize(12);
        cell.setAlign(HorizontalAlignment.RIGHT);

        tableTitre.draw();

        yPosition=yPosition-tableTitre.getHeaderAndDataHeight();


        BaseTable tableInfo = new BaseTable(yPosition, yStart,
                bottomMargin, tableWidth, margin, document, page, false, drawContent);
        headerRow = tableInfo.createRow(50);
        // the first parameter is the cell width
        cell = headerRow.createCell(100, "Informations du Client");
        cell.setFont(fontBold);
        cell.setFontSize(20);
        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        // border style
        cell.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        tableInfo.addHeaderRow(headerRow);

        row = tableInfo.createRow(20);
        cell = row.createCell(20, "Nom :");
        cell.setFontSize(12);
        cell = row.createCell(40, "NOM+PRENOM");
        cell.setFontSize(12);
        cell.setFont(fontBold);
        cell = row.createCell(20, "Facture n° :");
        cell.setFontSize(12);
        cell = row.createCell(20, "NOFACTURE");
        cell.setFontSize(12);
        cell.setFont(fontBold);

        row = tableInfo.createRow(20);
        cell = row.createCell(20, "Commande n° :");
        cell.setFontSize(12);
        cell = row.createCell(40, "NOCOMMANDE");
        cell.setFontSize(12);
        cell.setFont(fontBold);
        cell = row.createCell(20, "Date :");
        cell.setFontSize(12);
        cell = row.createCell(20, "DATEDUJOUR");
        cell.setFontSize(12);
        cell.setFont(fontBold);

        tableInfo.draw();

        yPosition=yPosition-tableInfo.getHeaderAndDataHeight();


        BaseTable tableCommande = new BaseTable(yPosition, yStart,
                bottomMargin, tableWidth, margin, document, page, true, drawContent);

        // the parameter is the row height
        headerRow = tableCommande.createRow(20);
        // the first parameter is the cell width
        cell = headerRow.createCell(15, "Type");
        cell.setFont(fontBold);
        cell.setFontSize(14);
        cell.setFillColor(Color.LIGHT_GRAY);

        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setAlign(HorizontalAlignment.CENTER);

        cell = headerRow.createCell(35, "Titre");
        cell.setFont(fontBold);
        cell.setFontSize(14);
        cell.setFillColor(Color.LIGHT_GRAY);

        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setAlign(HorizontalAlignment.CENTER);

        cell = headerRow.createCell(15, "Prix Unit");
        cell.setFont(fontBold);
        cell.setFontSize(14);
        cell.setFillColor(Color.LIGHT_GRAY);

        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setAlign(HorizontalAlignment.CENTER);

        cell = headerRow.createCell(15, "Quantité");
        cell.setFont(fontBold);
        cell.setFontSize(14);
        cell.setFillColor(Color.LIGHT_GRAY);

        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setAlign(HorizontalAlignment.CENTER);

        cell = headerRow.createCell(20, "Prix Total");
        cell.setFont(fontBold);
        cell.setFontSize(14);
        cell.setFillColor(Color.LIGHT_GRAY);

        // alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setAlign(HorizontalAlignment.CENTER);

        // border style
        tableCommande.addHeaderRow(headerRow);


        for(int i=1;i<=10;i++){
            row = tableCommande.createRow(15);
            cell = row.createCell(15, "Typeprd");
            cell.setFontSize(11);
            // alignment
            cell.setValign(VerticalAlignment.MIDDLE);
            cell.setAlign(HorizontalAlignment.CENTER);

            cell = row.createCell(35, "nomprod");
            cell.setFontSize(11);
            // alignment
            cell.setValign(VerticalAlignment.MIDDLE);
            cell.setAlign(HorizontalAlignment.CENTER);

            cell = row.createCell(15, "prixunip");
            cell.setFontSize(11);
            // alignment
            cell.setValign(VerticalAlignment.MIDDLE);
            cell.setAlign(HorizontalAlignment.CENTER);

            cell = row.createCell(15, "qteprd");
            cell.setFontSize(11);
            // alignment
            cell.setValign(VerticalAlignment.MIDDLE);
            cell.setAlign(HorizontalAlignment.CENTER);

            cell = row.createCell(20, "prixtot");
            cell.setFontSize(11);
            // alignment
            cell.setValign(VerticalAlignment.MIDDLE);
            cell.setAlign(HorizontalAlignment.CENTER);

        }

        row = tableCommande.createRow(20);
        cell = row.createCell(80, "Total");
        cell.setFontSize(11);
        cell.setFillColor(Color.LIGHT_GRAY);
        cell = row.createCell(20, "somme");
        cell.setFontSize(11);
        cell.setFillColor(Color.LIGHT_GRAY);
        row = tableCommande.createRow(20);
        cell = row.createCell(80, "Réduction");
        cell.setFontSize(11);
        cell = row.createCell(20, "%%%");
        cell.setFontSize(11);
        row = tableCommande.createRow(20);
        cell = row.createCell(80, "Montant Final");
        cell.setFontSize(11);
        cell = row.createCell(20, "montant");
        cell.setFontSize(11);

        tableCommande.draw();

        float tableHeight = tableTitre.getHeaderAndDataHeight();
        System.out.println("tableHeight = "+tableHeight);

        // close the content stream
        cos.close();

        // Save the results and ensure that the document is properly closed:
        document.save(outputFileName);
        document.close();

    }

    /**
     * modification du produit selectionner dans panier
     */
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

    /**
     * @param titreProduit
     * @return tarif de produit du nom titreProduit
     */
    private double tarifJournalier(String titreProduit) {
        double d=0;
        for(int i=0;i<listeProduits.size();i++){
            if(listeProduits.get(i).getTitreProduit().equals(titreProduit)){
                return listeProduits.get(i).getTarifProduit();
            }
        }
        return d;
    }

    /**
     * actvier le bouton modifier et bouton retirer dans le cas ou un produit dans le panier est selectionné
     * et remplir les champs de spinner et date fin location
     * @param event
     */
    @FXML
    void selectionDePanier(MouseEvent event) {
        if(tablePanier.getSelectionModel().getSelectedIndex()!=-1){
            remplirChampsInfos();
            modifier.setDisable(false);
            retirer.setDisable(false);
        }
    }

    /**
     * remplir les champs de spinner et date fin location
     */
    private void remplirChampsInfos() {
        ProduitPanier pp=tablePanier.getSelectionModel().getSelectedItem();
        int qt=pp.getQuantite();
        spinnerQuantite.getValueFactory().setValue(qt);

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,pp.getDuree());
        LocalDate d= LocalDate.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
        date.setValue(d);
    }

    /**
     * retirer le produit selectionner dans le panier de la liste on calculant le prix totale de nouveau
     * @param event
     */
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

    /**
     * ajouter un produit selectionner sur la liste du panier
     */
    public void ajoutAuPanier(){
        Produit pd=tableProduits.getSelectionModel().getSelectedItem();
        //date du jour
        Date d1=new Date();
        //date de fin location
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
        valid.setDisable(false);
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
        valid.setDisable(true);
        montantTotale.setText("0 €");
    }
    public void clientChoisi() throws SQLException {
        if(comboClient.getSelectionModel().getSelectedIndex()!=-1){
            partiePanier.setDisable(false);
            retirer.setDisable(true);
        }
        valid.setDisable(listePanier.size()==0);
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
