package Controlleur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    public Connection connection;

    /**
     * @return Fait la connection à la bdd
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException
    {
        String dbName = "videotheque";
        String userName = "root";
        String password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}