package Controlleur;

import DAO.TableauDAO;
import DAO.TableauDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EcouteurTableauDeBord implements Initializable {
    ObservableList<PieChart.Data> pieChartData;
    TableauDAO td = new TableauDAOImpl();
    @FXML
    private BarChart<Integer, String> barChart;
    @FXML
    private PieChart pieChart;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private String path;

    public EcouteurTableauDeBord() throws SQLException
    {
    }

    public void retour(ActionEvent e) throws IOException
    {
        path = "/Vue/SceneBienvenue.fxml";
        root = FXMLLoader.load(getClass().getResource(path));
        basculeScene(e);
    }

    public void basculeScene(ActionEvent e) throws IOException
    {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void remplirBarChart() throws SQLException
    {
        XYChart.Series set = new XYChart.Series<String, Integer>();
        td.remplirBar(set);
        barChart.getData().addAll(set);
    }

    private void remplirPieChart() throws SQLException
    {
        pieChartData = FXCollections.observableArrayList();
        td.remplirPie(pieChartData);
        pieChart.setData(pieChartData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try {
            remplirBarChart();
            remplirPieChart();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
