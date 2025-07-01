package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/spynetdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Constructor privado para patrón Singleton
    private DatabaseConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos MySQL");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: No se pudo conectar a la base de datos");
            e.printStackTrace();
        }
    }
    
    // Método para obtener la instancia única
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    // Método para obtener la conexión
    public Connection getConnection() {
        try {
            // Verificar si la conexión está activa
            if (connection == null || connection.isClosed()) {
                // Recrear la conexión si está cerrada
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la conexión");
            e.printStackTrace();
        }
        return connection;
    }
    
    // Método para cerrar la conexión
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }
}
