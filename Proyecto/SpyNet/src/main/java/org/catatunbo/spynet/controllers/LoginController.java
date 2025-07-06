package org.catatunbo.spynet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.catatunbo.spynet.App;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private ComboBox<String> roleComboBox;

    private LoginDAO loginDAO;

    public void initialize() {
        loginDAO = new LoginDAO();

        // Inicializar el ComboBox con los roles disponibles
        if (roleComboBox != null) {
            roleComboBox.getItems().addAll("admin", "auditor", "inspector", "cliente");
            roleComboBox.setValue("cliente"); // Valor por defecto
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor complete todos los campos", Alert.AlertType.ERROR);
            return;
        }

        if (loginDAO.validateUser(username, password)) {
            // Obtener información del usuario incluyendo su rol
            Login userInfo = loginDAO.getUserInfo(username);

            if (userInfo != null) {
                String role = userInfo.getRole();
                showAlert("Éxito", "Login exitoso! Bienvenido " + username +
                        "\nRol: " + role, Alert.AlertType.INFORMATION);

                // Navegar al dashboard correspondiente según el rol
                try {
                    navigateToDashboard(role, username);
                } catch (Exception e) {
                    showAlert("Error", "Error al cargar el dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            } else {
                showAlert("Error", "Error al obtener información del usuario", Alert.AlertType.ERROR);
            }

        } else {
            showAlert("Error", "Credenciales incorrectas o usuario inactivo", Alert.AlertType.ERROR);
        }
    }

    private void navigateToDashboard(String role, String username) throws Exception {
        String dashboardFile = "";

        switch (role.toLowerCase()) {
            case "admin":
                dashboardFile = "/fxml/admin/adminMainPanel.fxml";
                break;
            case "auditor":
                dashboardFile = "/fxml/admin/auditMainPanel.fxml";
                break;
            case "inspector":
                dashboardFile = "/fxml/admin/inspectorMainPanel.fxml";
                break;
            case "cliente":
                dashboardFile = "/fxml/admin/clientMainPanel.fxml";
                break;
            default:
                dashboardFile = "/fxml/admin/clientMainPanel.fxml"; // Default fallback
                break;
        }

        // Cambiar a la pantalla del dashboard correspondiente
        App.setRoot(dashboardFile);

    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = (roleComboBox != null) ? roleComboBox.getValue() : "cliente";

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor complete todos los campos", Alert.AlertType.ERROR);
            return;
        }

        if (role == null || role.isEmpty()) {
            showAlert("Error", "Por favor seleccione un rol", Alert.AlertType.ERROR);
            return;
        }

        if (loginDAO.registerUser(username, password, role)) {
            showAlert("Éxito", "Usuario registrado exitosamente con rol: " + role, Alert.AlertType.INFORMATION);
            usernameField.clear();
            passwordField.clear();
            if (roleComboBox != null) {
                roleComboBox.setValue("cliente");
            }
        } else {
            showAlert("Error", "No se pudo registrar el usuario. Puede que ya exista.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }
}
