package org.catatunbo.spynet.dao;

import org.catatunbo.spynet.Auditory;
import org.catatunbo.spynet.database.DatabaseConnection;
import org.catatunbo.spynet.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    /**
     * Authenticate user with given username
     * @param username
     * @return User data if found
     */
    public User authenticate(String username) {
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

    /**
     * Adds the recieved ser to the database
     * @param user New user
     * @return Number of rows affected
     */
    public int addToDataBase(User user) throws SQLException{
        String query = """
                INSERT INTO user (
                    username, 
                    user_password_hash, 
                    password_salt, 
                    user_rol, 
                    user_state, 
                    user_date_register,
                    user_last_session
                    )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query); 
        
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPasswordHash());
        statement.setString(3, user.getPasswordSalt());
        statement.setString(4, user.getUserRole());
        statement.setString(5, user.getUserState());
        statement.setString(6, user.getDateRegister());
        statement.setString(7, user.getLastSession());
        
        return statement.executeUpdate();
    }

    public List<User> getLimitedUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM all_users";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("user_rol"),
                        rs.getString("user_last_session"),
                        rs.getString("user_state")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error en imported auditories: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
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

    public boolean assignRoleToUser(int userId, String role) {
        String sql = "UPDATE user SET user_rol = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    
}