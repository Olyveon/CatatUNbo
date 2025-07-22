package org.catatunbo.spynet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.catatunbo.spynet.dao.AuditoryDAO;
import org.catatunbo.spynet.Session;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Timestamp;

public class AdminCreateAuditController {

    @FXML
    private TextField txtNombreEmpresa;
    
    @FXML
    private TextField txtFechaLimite;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private Button btnEnviar;

    @FXML
    private void initialize() {
        // Inicialización del controlador si es necesario
        System.out.println("AdminCreateAuditController inicializado");
    }

    @FXML
    private void handleEnviar(ActionEvent event) {
        String nombreEmpresa = txtNombreEmpresa.getText().trim();
        String fechaLimite = txtFechaLimite.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        
        if (nombreEmpresa.isEmpty() || fechaLimite.isEmpty() || descripcion.isEmpty()) {
            showAlert("Campos Incompletos", "Por favor complete todos los campos.", Alert.AlertType.WARNING);
            return;
        }
        
        Timestamp fechaLimiteTimestamp;
        try {
            LocalDate fechaLocal = LocalDate.parse(fechaLimite, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime fechaHora = fechaLocal.atTime(23, 59, 59);
            fechaLimiteTimestamp = Timestamp.valueOf(fechaHora);
        } catch (DateTimeParseException e) {
            showAlert("Formato de Fecha Inválido", "Por favor ingrese la fecha en formato yyyy-MM-dd (ejemplo: 2024-12-31)", Alert.AlertType.ERROR);
            return;
        }
        
        if (fechaLimiteTimestamp.before(new Timestamp(System.currentTimeMillis()))) {
            showAlert("Fecha Inválida", "La fecha límite no puede ser en el pasado.", Alert.AlertType.WARNING);
            return;
        }
        
        AuditoryDAO auditoryDAO = new AuditoryDAO();
        
        try {
            int clientId = auditoryDAO.findOrCreateClient(nombreEmpresa);
            if (clientId == -1) {
                showAlert("Error", "No se pudo crear o encontrar el cliente.", Alert.AlertType.ERROR);
                return;
            }
            
            String auditoryName = "Auditoría - " + nombreEmpresa;
            int auditoryId = auditoryDAO.createAuditory(auditoryName, clientId, fechaLimiteTimestamp);
            if (auditoryId == -1) {
                showAlert("Error", "No se pudo crear la auditoría.", Alert.AlertType.ERROR);
                return;
            }
            
            // 3. La auditoría se crea sin auditor asignado (será asignado posteriormente por un inspector)
            
            // 4. Crear observación inicial con el usuario admin que creó la auditoría
            int currentUserId = Session.getInstance().getCurrentUser().getUserId();
            boolean observationCreated = auditoryDAO.insertObservation(auditoryId, currentUserId, 
                "inicial_description", descripcion);
            if (!observationCreated) {
                System.err.println("Advertencia: No se pudo crear la observación inicial.");
            }
            
            // Mostrar mensaje de éxito
            showAlert("Auditoría Creada", "La auditoría se ha creado exitosamente.\nID: " + auditoryId + 
                "\nCliente: " + nombreEmpresa + "\nFecha límite: " + fechaLimite, Alert.AlertType.INFORMATION);
            
            // Limpiar formulario
            txtNombreEmpresa.clear();
            txtFechaLimite.clear();
            txtDescripcion.clear();
            
        } catch (Exception e) {
            System.err.println("Error inesperado al crear auditoría: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error Inesperado", "Ocurrió un error al crear la auditoría: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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

    @FXML
    private void handleConsultarAuditorias(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminMainPanel.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Muestra una alerta con el título, mensaje y tipo especificados
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
