package liakh.olga.task;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private final String HOST = "jdbc:postgresql://localhost:5432/equations";
    private final String USERNAME = "postgres";
    private final String PASSWORD = "020980";
    private final String DB_NAME = "equations";

    private Connection connection;

    public DBConnector() {
        try {
            connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
