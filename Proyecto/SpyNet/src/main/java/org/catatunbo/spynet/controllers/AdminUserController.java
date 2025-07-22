package org.catatunbo.spynet.controllers;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.catatunbo.spynet.User;
import org.catatunbo.spynet.dao.UserDAO;

import java.io.IOException;

public class AdminUserController {

    @FXML private TableView<User> tableUser;
    @FXML private TableColumn<User, String> colID;
    @FXML private TableColumn<User, String> colUsuario;
    @FXML private TableColumn<User, String> colRol;
    @FXML private TableColumn<User, String> colUltimaSes;
    @FXML private TableColumn<User, String> colEstado;

    @FXML private Button btnAsignarRol;
    @FXML private TextField txtID;

    @FXML private ComboBox<String> comboRol;

    @FXML
    private void initialize() {
        comboRol.getItems().addAll("admin","auditor","inspector","cliente");

        colID.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        colUltimaSes.setCellValueFactory(new PropertyValueFactory<>("lastSession"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("userState"));

        UserDAO userDAO = new UserDAO();
        tableUser.setItems(FXCollections.observableArrayList(userDAO.getLimitedUsers()));
    }
    @FXML
    private void handleAssignRole(ActionEvent event) {
        String selectedRole = comboRol.getValue();
        String userIdText = txtID.getText();

        if (selectedRole == null || userIdText.isEmpty()) {
            System.out.println("Please select a role and enter a user ID.");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdText);
        } catch (NumberFormatException e) {
            showAlert("Error de Conexión", "ID invalido " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.assignRoleToUser(userId, selectedRole);

        if (success) {
            System.out.println("Role assigned successfully.");
            tableUser.setItems(FXCollections.observableArrayList(userDAO.getLimitedUsers()));
        } else {
            showAlert("Error de Asignacion", "ID invalido", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleReturn(ActionEvent event) {
        // Ya estamos en el panel de Asignar Roles, no necesitamos navegar a otra pantalla
        System.out.println("Ya estás en el panel de Asignar Roles");
        showAlert("Información", "Ya estás en el panel de asignación de roles", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleRealizarAuditoria(ActionEvent event) throws IOException {
        // Navegar al panel de realizar auditoría (adminCreatePanel)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/adminCreatePanel.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleExit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
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
