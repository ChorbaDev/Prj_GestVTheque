package Controlleur;

import Modele.Client;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EcouteurListeClients implements Initializable {

    /*Concerne la liste*/
    @FXML private TableView<Client> table;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colPrenom;
    @FXML private TableColumn<Client, String> colMail;
    @FXML private TableColumn<Client, Boolean> colFidele;
    ObservableList<Client> obList= FXCollections.observableArrayList();

    /*Concerne la scene*/
    @FXML private JFXTextField nom;
    @FXML private JFXTextField prenom;
    @FXML private JFXCheckBox fidele;
    @FXML private JFXTextField mail;
    @FXML private ImageView image;
    private FileInputStream fis=new FileInputStream(new File("src/Images/Photo-non-disponible.jpg"));
    private Boolean uneImageEstSelectionner=false;
    public EcouteurListeClients() throws FileNotFoundException {
    }

    public void remplirLaListe() throws SQLException
    {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT idClient,nomClient,prenomClient,clientFidele,mailClient FROM client";
        ResultSet res=connection.createStatement().executeQuery(sql);
        while( res.next() )
        {
            obList.add(new Client(
                                  res.getInt("idClient"),
                                  res.getString("nomClient"),
                                  res.getString("prenomClient"),
                                  res.getString("mailClient"),
                                  res.getBoolean("clientFidele")
                                 ));
        }

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
        if(!table.getSelectionModel().isEmpty()) {
            Client clientSupprmier = table.getSelectionModel().getSelectedItem();
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            String sql = "DELETE FROM client where idClient='" + clientSupprmier.getIdClient() + "'";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            notifBuilder("Opération réussie",
                    "Votre opération de suppression du " + nom.getText() + " est éffectué avec succès.",
                    "/Images/checked.png");
            nettoyageScene();
        }
    }
    public void ajoutClient() throws SQLException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT nomClient FROM client WHERE mailClient='"+mail.getText()+"'";
        Statement statement = connection.createStatement();
        if(statement.executeQuery(sql).next()){
            notifBuilder("Attention",
                    "le mail "+mail.getText()+" déja existe dans la base de données.",
                    "/Images/warning.png");
            statement.close();
        }
        else{
            int fid=fidele.isSelected()?1:0;
            String insertReq="INSERT INTO client (nomClient,prenomClient,clientFidele,pdp,mailClient) values (?,?,?,?,?)";
            PreparedStatement statInsert = connection.prepareStatement(insertReq);
            statInsert.setString(1,nom.getText());
            statInsert.setString(2,prenom.getText());
            statInsert.setInt(3,fid);

            if(uneImageEstSelectionner){
                statInsert.setBinaryStream(4,fis);
                uneImageEstSelectionner=false;
            }
            else{
                InputStream is=inputClient(new Client(nom.getText(),prenom.getText(),mail.getText(),fidele.isSelected()));
                statInsert.setBinaryStream(4,is);
            }

            statInsert.setString(5,mail.getText());
            statInsert.executeUpdate();
            statInsert.close();
            notifBuilder("Opération réussie",
                    "Votre opération d'ajouter le client "+nom.getText()+" est éffectué avec succès.",
                    "/Images/checked.png");
            nettoyageScene();
        }

    }
    public void ChoisirUneImage() throws FileNotFoundException {
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
    public void viderImage() throws IOException {
        File file=new File("src/Images/Photo-non-disponible.jpg");
        fis=new FileInputStream(file);
        image.setImage(new Image("/Images/Photo-non-disponible.jpg"));
        uneImageEstSelectionner=true;
    }

    public void modifierClient() throws SQLException, IOException {
        if(!table.getSelectionModel().isEmpty()){
            Client client=table.getSelectionModel().getSelectedItem();
            Client clientSelectionner=new Client(client.getIdClient(),nom.getText(),prenom.getText(),mail.getText(),fidele.isSelected());
            if(!client.equals(clientSelectionner) || uneImageEstSelectionner) {
               modifierclientSelectionner(clientSelectionner);
               notifBuilder("Opération réussie",
                       "Votre opération de modifier le client "+client.getNom()+" a réussie.",
                       "/Images/checked.png");
           }
        }
        else{
            notifBuilder("Attention",
                        "Il faut sélectionner un client pour pouvoir le modifier.",
                        "/Images/warning.png");
        }

    }

    private void modifierclientSelectionner(Client cl) throws SQLException, IOException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        int fid=cl.isClientFidele()?1:0;
        String sql="update client set nomClient=? , prenomClient=? , mailClient=? ,clientFidele=?,pdp=? where idClient=?";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,cl.getNom());
        statement.setString(2,cl.getPrenom());
        statement.setString(3,cl.getMailClient());
        statement.setInt(4,fid);
        if(uneImageEstSelectionner){
            statement.setBinaryStream(5,fis);
            uneImageEstSelectionner=false;
        }
        else{
            InputStream is=inputClient(cl);
            statement.setBinaryStream(5,is);
        }
        statement.setInt(6,cl.getIdClient());
        statement.executeUpdate();
        statement.close();
        nettoyageScene();
    }

    private InputStream inputClient(Client cl) throws SQLException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sqlTEST="SELECT pdp from client where idClient="+cl.getIdClient();
        Statement stat= connection.createStatement();
        ResultSet res=stat.executeQuery(sqlTEST);
        res.next();
        InputStream is=res.getBinaryStream("pdp");
        return is;
    }

    public void selectionClient() throws SQLException, IOException {
        viderChamps();
        if(!table.getSelectionModel().isEmpty()){
            Client clientSelectionner=table.getSelectionModel().getSelectedItem();
            selectionImage(clientSelectionner);
            nom.setText(clientSelectionner.getNom());
            prenom.setText(clientSelectionner.getPrenom());
            mail.setText(clientSelectionner.getMailClient());
            if(clientSelectionner.isClientFidele()){
                fidele.setSelected(true);
            }
        }
    }
    private Image imageClient(Client cl) throws SQLException, IOException {
        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT pdp from client where idClient="+cl.getIdClient();
        ResultSet res=connection.createStatement().executeQuery(sql);
        res.next();
        InputStream is=res.getBinaryStream("pdp");
        OutputStream os=new FileOutputStream(new File("photo.jpg"));
        byte[] content=new byte[1024];

        int size=0;
        while( (size = is.read(content))!=-1 ){
            os.write(content,0,size);
        }
        os.close();
        is.close();
        Image img=new Image("file:photo.jpg",400,300,true, true);
        return img;
    }
    private void selectionImage( Client cl) throws SQLException, IOException {
        image.setImage(imageClient(cl));
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
    private void viderChamps() {
        mail.setText("");
        nom.setText("");
        prenom.setText("");
        image.setImage(new Image("/Images/Photo-non-disponible.jpg"));
        fidele.setSelected(false);
    }
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;
    public void retour(ActionEvent e) throws IOException {
        path="/Vue/SceneBienvenue.fxml";
        basculeScene(e,path);
    }
    public void ensembleDesCommandes(ActionEvent e) throws IOException {
        path="/Vue/SceneCommandesClient.fxml";
        basculeScene(e,path);
    }
    public void basculeScene(ActionEvent e,String pathURL) throws IOException {
        root = FXMLLoader.load(getClass().getResource(pathURL));
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<Client,Integer>("idClient"));
        colNom.setCellValueFactory(new PropertyValueFactory<Client,String>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<Client,String>("prenom"));
        colMail.setCellValueFactory(new PropertyValueFactory<Client,String>("mailClient"));
        colFidele.setCellValueFactory(new PropertyValueFactory<Client,Boolean>("clientFidele"));
        table.setItems(obList);
        try {
            remplirLaListe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
