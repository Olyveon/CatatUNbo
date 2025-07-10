package org.catatunbo.spynet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    static {
        loadDatabaseProperties();
    }
    
    private static void loadDatabaseProperties() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream("database_resources/database.properties")) {
            if (is != null) {
                props.load(is);
                URL = props.getProperty("database.url", "jdbc:mysql://localhost:3306/spynetdb");
                USERNAME = props.getProperty("database.username", "root");
                PASSWORD = props.getProperty("database.password", "");
            } else {
                // 
                URL = "jdbc:mysql://localhost:3306/spynetdb";
                USERNAME = "root";
                PASSWORD = "";
                System.out.println("Archivo database.properties no encontrado, usando valores por defecto");
            }
        } catch (IOException e) {
            // Valores por defecto en caso de error
            URL = "jdbc:mysql://localhost:3306/spynetdb";
            USERNAME = "root";
            PASSWORD = "";
            System.err.println("Error cargando database.properties: " + e.getMessage());
        }
    }
    
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUrl = URL + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            this.connection = DriverManager.getConnection(connectionUrl, USERNAME, PASSWORD);
            System.out.println("Conexión a la base de datos establecida exitosamente");
        } catch (ClassNotFoundException ex) {
            System.err.println("Database Connection Creation Failed: " + ex.getMessage());
            throw new SQLException("MySQL Driver not found", ex);
        } catch (SQLException ex) {
            System.err.println("Error conectando a la base de datos: " + ex.getMessage());
            throw ex;
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexión a la base de datos cerrada");
        }
    }
    
    // Mé   todo para probar la conexión
    public static boolean testConnection() {
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            return !dbConnection.getConnection().isClosed();
        } catch (SQLException e) {
            System.err.println("Error en test de conexión: " + e.getMessage());
            return false;
        }
    }
}
