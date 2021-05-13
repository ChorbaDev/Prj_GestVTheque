package Controlleur;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class EcouteurRealisateur {

    /*concerne la liste réalisateur*/
    @FXML
    private TableView<?> tblRealisateur;

    @FXML
    private TableColumn<?, ?> colIdRealisateur;

    @FXML
    private TableColumn<?, ?> colNomRealisateur;

    @FXML
    private TableColumn<?, ?> colPrenomRealisateur;

    @FXML
    private TableColumn<?, ?> colNbDVD;

    /*concerne la scene réalisateur*/
    @FXML
    private JFXTextField edtNomRealisateur;

    @FXML
    private JFXTextField edtPrenomRealisateur;

    @FXML
    private JFXTextArea mmoResumeRealisateur;
}
