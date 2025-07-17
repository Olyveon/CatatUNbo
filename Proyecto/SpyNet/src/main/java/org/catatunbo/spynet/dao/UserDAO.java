package org.catatunbo.spynet.dao;

import org.catatunbo.spynet.database.DatabaseConnection;
import org.catatunbo.spynet.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO {
    
    public User authenticate(String username) {
        // Authenticate user with given username and password
        String sql = "SELECT * FROM user WHERE username = ? AND user_state = 'ACTIVO'";
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql); 
            statement.setString(1, username);
            
            ResultSet resultSet = statement.executeQuery(); // Select users
            
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("user_password_hash"));
                user.setPasswordSalt(resultSet.getString("password_salt"));
                user.setUserRole(resultSet.getString("user_rol"));
                user.setUserState(resultSet.getString("user_state"));
                user.setDateRegister(resultSet.getString("user_date_register"));
                user.setLastSession(resultSet.getString("user_last_session"));
                
                
                updateLastSession(user.getUserId());
                
                return user;
            } else {
                System.out.println("No se encontró el usuario en la base de datos");
            }
            
        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
            e.printStackTrace(); 
        }
        
        return null;
    }


    private void updateLastSession(int userId) {
        String sql = "UPDATE user SET user_last_session = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar última sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }


    
}