package DAO;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;

public interface TableauDAO {
        void remplirBar(XYChart.Series set)throws SQLException;
        void remplirPie(ObservableList<PieChart.Data> pieChartData)throws SQLException;
}
