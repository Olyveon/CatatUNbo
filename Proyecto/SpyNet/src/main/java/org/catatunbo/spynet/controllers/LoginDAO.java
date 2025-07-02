package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
    
    private DatabaseConnection dbConnection;
    
    public LoginDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Valida las credenciales del usuario contra la base de datos
     * @param username nombre de usuario
     * @param password contraseña
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean validateUser(String username, String password) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ? AND user_password_hash = ? AND user_state = 'ACTIVO'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // En producción deberías usar hash de la contraseña
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Registra un nuevo usuario en la base de datos
     * @param username nombre de usuario
     * @param password contraseña
     * @param role rol del usuario
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registerUser(String username, String password, String role) {
        // Primero verificar si el usuario ya existe
        if (userExists(username)) {
            System.out.println("El usuario ya existe");
            return false;
        }
        
        String query = "INSERT INTO user (username, user_password_hash, user_rol, user_state) VALUES (?, ?, ?, 'ACTIVO')";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // En producción deberías usar hash de la contraseña
            pstmt.setString(3, role);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verifica si un usuario ya existe en la base de datos
     * @param username nombre de usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar si el usuario existe: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtiene la información completa del usuario
     * @param username nombre de usuario
     * @return objeto Login con la información del usuario, null si no existe
     */
    public Login getUserInfo(String username) {
        String query = "SELECT username, user_password_hash, user_rol FROM user WHERE username = ? AND user_state = 'ACTIVO'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Login(
                        rs.getString("username"),
                        rs.getString("user_password_hash"),
                        rs.getString("user_rol")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener información del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
