package org.catatunbo.spynet.controllers;

import javafx.fxml.FXML;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDate;

import org.catatunbo.spynet.PasswordObject;
import org.catatunbo.spynet.User;
import org.catatunbo.spynet.dao.UserDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateUserController {

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;
    
    @FXML
    private Button loginButton;

    private UserDAO userDAO;

    private PasswordHasher passwordHasher;

    public CreateUserController() {
        this.userDAO = new UserDAO();
        this.passwordHasher = new PasswordHasher();
    }

    /**
     * Verifies the new user data, assigns him the basic roles
     * and adds it to the "user" table in the database
     * @param event Event recieved form "userCreationButton" activation
     */
    @FXML
    private void handleUserCreation(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (isValidInput(username, password, confirmPassword)) {
            User newUser = createNewClient(username, confirmPassword);
            
            try {
                userDAO.addToDataBase(newUser);
            } catch (SQLException e) {
                showAlert("Usuario No Válido", 
                "Cambie el nombre de usuario por uno inutilizado",
                Alert.AlertType.ERROR);
                // NOTE: TMP
                e.printStackTrace();
                return;
            }

            showAlert("Usuario Creado Exitosamente", 
            "Ahora puede ingresar con su nuevo usuario", 
            Alert.AlertType.INFORMATION);

            loginButton.fire();
        }
    }

    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();        
    }

    public User createNewClient(String username, String password) {
        PasswordObject passwordObject = new PasswordObject(password);
        String hashedPassword = null;
        
         try {
            hashedPassword = passwordHasher.hashPassword(passwordObject);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            showAlert("Error en hasheo de clave", 
            "Mecanimos para asegurar la clave fallaron",
            Alert.AlertType.ERROR);
            return null;
        }
        
        return new User(-1, 
                        username, 
                        hashedPassword, 
                        passwordObject.getSalt(),
                        "cliente",
                        "ACTIVO",
                        LocalDate.now().toString(),
                        LocalDate.now().toString()
                        );
    }   

    private boolean isValidInput(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Datos Insuficientes", 
            " Porfavor llene correctamente todos los campos", 
            Alert.AlertType.ERROR);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Verifique Contraseña", 
            "No se ingresó consistentemente la contraseña", 
            Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

