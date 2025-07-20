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
import org.catatunbo.spynet.PasswordObject;
import org.catatunbo.spynet.Session;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;

    private PasswordHasher passwordHasher;

    public LoginController() {
        this.userDAO = new UserDAO();
        this.passwordHasher = new PasswordHasher();
    }

    /**
     * Verifies if input data is complete, if so, get corresponding data
     * from the given user and authenticates it identity.
     * @param event Event thrown with "Iniciar Sesión" button is activated
     * @throws IOException Exception if it's not possible to switch to the "userCreation" or "main" scene
     */
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor ingrese usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        try {
            User user = userDAO.authenticate(username);
            PasswordObject enteredPassword = new PasswordObject(password, user.getPasswordSalt());
            String enteredPasswordHashed = this.passwordHasher.hashPassword(enteredPassword);

            if (user != null && enteredPasswordHashed.equals(user.getPasswordHash())) {                
                switchToUserPanel(user, (Node) event.getSource());
            } else {
                showAlert("Login Fallido", "Usuario o contraseña incorrectos, o cuenta bloqueada.", Alert.AlertType.ERROR);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            showAlert("No se pudo verificar la contarseña", 
            "Los métodos para verificar la contarseña han fallado", 
            Alert.AlertType.ERROR);
        } catch (IOException e) {
            showAlert("No se pudo cambiar de escena", 
            "La escena para el usuario dado no esta lista", 
            Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error de Conexión", "No se pudo conectar a la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToUserCreation(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/createUser.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();
    }

    private void switchToUserPanel(User user, Node source) throws IOException {
        Session.getInstance().setCurrentUser(user);    
        FXMLLoader loader = new FXMLLoader(getClass().getResource(chooseStage(user)));
        Parent root = loader.load();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();
    }

    private String chooseStage(User user) {
        switch (user.getUserRole()) {
            case "admin":
                return "/fxml/admin/adminCreateAuditPanel.fxml";
            case "inspector":
                return "/fxml/inspector/inspectorMainPanel.fxml";
            default:
                return null;
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}