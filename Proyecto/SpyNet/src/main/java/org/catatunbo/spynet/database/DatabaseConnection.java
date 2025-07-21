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
        // Para correr al aplicación con RUN, primero compilar (mvn clen complie)
        System.out.println("HOLAAAAAAAAAAAAA "+DatabaseConnection.class.getClassLoader().getResourceAsStream("database_resources/database.properties"));

        try (InputStream is = DatabaseConnection.class.getResourceAsStream("/database_resources/database.properties")) {
            if (is != null) {
                props.load(is);
                URL = props.getProperty("database.url");
                USERNAME = props.getProperty("database.username");
                PASSWORD = props.getProperty("database.password");
                
                if (URL == null || USERNAME == null) {
                    throw new RuntimeException("Propiedades requeridas faltantes en database.properties: " +
                        "database.url=" + URL + ", database.username=" + USERNAME);
                }
            } else {
                throw new RuntimeException("Archivo database.properties no encontrado en classpath. " +
                    "Verifique que el archivo esté en src/main/resources/database_resources/database.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar database.properties: " + e.getMessage(), e);
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
}
