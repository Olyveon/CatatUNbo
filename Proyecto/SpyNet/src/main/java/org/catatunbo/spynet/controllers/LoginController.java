package org.catatunbo.spynet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import org.catatunbo.spynet.dao.UserDAO;
import org.catatunbo.spynet.User;
import org.catatunbo.spynet.Session;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor ingrese usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        try {
            
            User user = userDAO.authenticate(username, password);
            
            if (user != null) {                
                Session.getInstance().setCurrentUser(user);    
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/adminMainPanel.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 1280, 800));
                stage.show();
                
            } else {
                showAlert("Login Fallido", "Usuario o contraseña incorrectos, o cuenta bloqueada.", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            showAlert("Error de Conexión", "No se pudo conectar a la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToUserCreation(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/createUser.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}