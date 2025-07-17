package org.catatunbo.spynet.controllers;

import javafx.fxml.FXML;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    private Button userCreationButton;

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

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Datos Insuficientes", 
            " Porfavor llene correctamente todos los campos", 
            Alert.AlertType.ERROR);
            return;
        }

        // TODO: check whether user is new or not

        if (!password.equals(confirmPassword)) {
            showAlert("Verifique Contraseña", 
            "No se ingresó consistentemente la contraseña", 
            Alert.AlertType.ERROR);
            return;
        }

        User newUser = createNewClient(username, confirmPassword);
        System.out.println("\n\n\t INT VALUE: " +userDAO.addToDataBase(newUser));
    }

    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();        
    }

    private User createNewClient(String username, String password) {
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
        
        return new User(0, 
                        username, 
                        hashedPassword, 
                        passwordObject.getSalt(),
                        "cliente",
                        "ACTIVO",
                        LocalDate.now().toString(),
                        LocalDate.now().toString()
                        );
    }   

    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

