package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    public static Connection databaseLink;

    public static Connection getConnection() {
        String databaseName = "ucusfirsati";
        String databaseUser = "postgres";
        String databasePassword = "cennet70";
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;

        try {
            // Load the JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish the database connection
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
