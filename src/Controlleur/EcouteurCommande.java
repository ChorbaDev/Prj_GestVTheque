package Controlleur;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class EcouteurCommande {
    @FXML
    private Spinner<?> spinnerQuantite;
    @FXML
    private JFXButton triBD;

    @FXML
    private JFXComboBox<?> comboClient;

    @FXML
    private TableView<?> tablePanier;

    @FXML
    private Label montantTotale;

    @FXML
    private Label reduction;

    @FXML
    private JFXToggleButton toggleFacture;

    @FXML
    private TableView<?> tableProduits;

    @FXML
    void annulerPanier(ActionEvent event) {

    }

    @FXML
    void disponible(ActionEvent event) {

    }

    @FXML
    void retirerProduit(ActionEvent event) {

    }

    @FXML
    void retour(ActionEvent event) {

    }

    @FXML
    void selectionDePanier(MouseEvent event) {

    }

    @FXML
    void triCD(ActionEvent event) {

    }

    @FXML
    void triDVD(ActionEvent event) {

    }

    @FXML
    void triDictionnaire(ActionEvent event) {

    }

    @FXML
    void triManuel(ActionEvent event) {

    }

    @FXML
    void triRoman(ActionEvent event) {

    }

    @FXML
    void validerPanier(ActionEvent event) {

    }

}
