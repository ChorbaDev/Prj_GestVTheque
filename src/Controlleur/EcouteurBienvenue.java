package Controlleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EcouteurBienvenue {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;

    public void listeProduits(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneListeProduits.fxml";
        basculeScene(e, path);
    }

    public void listeClients(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneListeClients.fxml";
        basculeScene(e, path);
    }

    public void listeAuteurs(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneAuteur.fxml";
        basculeScene(e, path);
    }

    public void tableauDeBord(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneTableauDeBord.fxml";
        basculeScene(e, path);
    }

    public void listeRealisateur(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneRealisateur.fxml";
        basculeScene(e, path);
    }

    public void commander(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneCommande.fxml";
        basculeScene(e, path);
    }

    public void basculeScene(ActionEvent e, String pathURL) throws IOException
    {
        root = FXMLLoader.load(getClass().getResource(pathURL));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
